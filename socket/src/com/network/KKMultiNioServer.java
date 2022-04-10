package com.network;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

public class KKMultiNioServer {
    public static HashMap<String,KnockKnockProtocol> kkpMap = new HashMap<>();
    public static void main(String[] args) throws IOException {
        Selector selector = Selector.open();
        ServerSocketChannel serverSocket = ServerSocketChannel.open();
        serverSocket.bind(new InetSocketAddress("127.0.0.1", 8888));
        serverSocket.configureBlocking(false);
        // register accept event to selector
        serverSocket.register(selector, SelectionKey.OP_ACCEPT);
        ByteBuffer buffer = ByteBuffer.allocate(256);
        while (true) {
            System.out.println("enter");
            selector.select();
            System.out.println("select return");
            Set<SelectionKey> selectedKeys = selector.selectedKeys();
            Iterator<SelectionKey> iter = selectedKeys.iterator();
            while (iter.hasNext()) {
                SelectionKey key = iter.next();
                if (key.isAcceptable()) {
                    System.out.println("is acceptable");
                    register(selector, serverSocket);
                }

                if (key.isReadable()) {
                    System.out.println("is readable");
                    answerWithEcho(buffer, key);
                }
                iter.remove();
            }
        }
    }

    private static void register(Selector selector, ServerSocketChannel serverSocket)
            throws IOException {
        SocketChannel client = serverSocket.accept();
        SocketAddress sa = client.getRemoteAddress();
        client.configureBlocking(false);
        client.register(selector, SelectionKey.OP_READ);
        KnockKnockProtocol kkp = getKKP(client.getRemoteAddress().toString());
        String outputLine = kkp.processInput(null);
        outputLine = outputLine + "\n";
        System.out.println(outputLine);
        ByteBuffer buffer1 = ByteBuffer.wrap(outputLine.getBytes("UTF-8"));
        System.out.println(buffer1);
        client.write(buffer1);
        buffer1.clear();
    }

    private static void answerWithEcho(ByteBuffer buffer, SelectionKey key)
            throws IOException {
        SocketChannel client = (SocketChannel) key.channel();
        client.read(buffer);
        System.out.println("read content:" + new String(trim(buffer.array()), "UTF-8") + "----");
        if (buffer.hasArray()) {
            buffer.flip();
            String newContent = new String(trim(buffer.array()), "UTF-8");
            if (newContent.endsWith("\n")) {
                newContent = newContent.substring(0,newContent.length()-2);
            }
            KnockKnockProtocol kkp = getKKP(client.getRemoteAddress().toString());
            System.out.println("read content:" + newContent);
            String output = kkp.processInput(newContent);
            System.out.println("write content:" + output);
            output = output + "\n";
            ByteBuffer buffer1 = ByteBuffer.wrap(output.getBytes("UTF-8"));
            client.write(buffer1);
            buffer.clear();
        }
    }

    /**
     * @param clientId
     * @return
     */
    public static KnockKnockProtocol getKKP(String clientId)
    {
        System.out.println("clientId:" + clientId);
        if (kkpMap.containsKey(clientId)) {
            return kkpMap.get(clientId);
        } else {
            KnockKnockProtocol kkp = new KnockKnockProtocol();
            kkpMap.put(clientId,kkp);
            return  kkp;
        }
    }
    public  static byte[] trim(byte[] bytes)
    {
        int i = bytes.length - 1;
        while (i >= 0 && bytes[i] == 0)
        {
            --i;
        }

        return Arrays.copyOf(bytes, i + 1);
    }
}
