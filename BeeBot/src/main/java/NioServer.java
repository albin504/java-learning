import com.aliyuncs.exceptions.ClientException;
import org.apache.commons.configuration.ConfigurationException;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Set;

/**
 * socket server，使用nio(基于epool) 、单线程
 */
public class NioServer {
    public static void main(String[] args) throws IOException {
        Selector selector = Selector.open();
        ServerSocketChannel serverSocket = ServerSocketChannel.open();
        serverSocket.bind(new InetSocketAddress("127.0.0.1", 8888));
        serverSocket.configureBlocking(false);
        // register accept event to selector
        serverSocket.register(selector, SelectionKey.OP_ACCEPT);
        while (true) {
            selector.select();
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
                    try {
                        answer(key);
                    } catch (ClientException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                iter.remove();
            }
        }
    }

    private static void register(Selector selector, ServerSocketChannel serverSocket) throws IOException {
        SocketChannel client = serverSocket.accept();
        SocketAddress sa = client.getRemoteAddress();
        client.configureBlocking(false);
        client.register(selector, SelectionKey.OP_READ);
        ByteBuffer buffer1 = ByteBuffer.wrap("有什么可以帮你的吗？\n".getBytes("UTF-8"));
        client.write(buffer1);
        buffer1.clear();
    }

    private static void answer(SelectionKey key) throws IOException, ClientException {
        SocketChannel client = (SocketChannel) key.channel();
        ByteBuffer buffer = ByteBuffer.allocate(256);
        client.read(buffer);
        if (buffer.position() == 0) {
            return;
        }
//        Logger.info(Arrays.toString(buffer.array()));
        if (buffer.hasArray()) {
            buffer.flip();
            String userInput = new String(trim(buffer.array()), "UTF-8");
            userInput = userInput.trim();
            BeeRot beeRot = BeeRot.getBeeBot(client.getRemoteAddress().toString());
            String responseTxt = null;
            try {
                responseTxt = beeRot.dealRequest(userInput);
            } catch (InterruptedException | ConfigurationException e) {
                e.printStackTrace();
            }
            responseTxt = responseTxt + "\n";
            ByteBuffer responseBuffer = ByteBuffer.wrap(responseTxt.getBytes("UTF-8"));
            client.write(responseBuffer);
        }
        buffer.clear();
    }

    public static byte[] trim(byte[] bytes) {
        int i = bytes.length - 1;
        while (i >= 0 && bytes[i] == 0) {
            --i;
        }
        return Arrays.copyOf(bytes, i + 1);
    }
}
