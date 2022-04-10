import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * socket server，使用nio(基于epool) 和线程池
 */
public class NioThreadPoolServer {
    public static void main(String[] args) throws IOException {
        Selector selector = Selector.open();
        ServerSocketChannel serverSocket = ServerSocketChannel.open();
        serverSocket.bind(new InetSocketAddress("127.0.0.1", 8889));
        serverSocket.configureBlocking(false);
        // register accept event to selector
        serverSocket.register(selector, SelectionKey.OP_ACCEPT);
        ExecutorService executor = Executors.newCachedThreadPool();
        while (true) {
            selector.select();
            Set<SelectionKey> selectedKeys = selector.selectedKeys();
            Logger.info("selected keys:" + selectedKeys.size());
            Iterator<SelectionKey> iter = selectedKeys.iterator();
            while (iter.hasNext()) {
                SelectionKey key = iter.next();
                if (key.isAcceptable()) {
                    System.out.println("is acceptable");
                    register(selector, serverSocket);
                }
                if (key.isReadable()) {
                    System.out.println("is readable");
                    SocketChannel client = (SocketChannel) key.channel();
                    Logger.info(client.getRemoteAddress().toString());
                    /**
                     * client.read() 操作，尽可能不要在子线程中进行。
                     * 否则就会导致问题：
                     * 经测试，当一个socket事件触发之后，如可读，如果socket中的数据没有被同步读取，下一次循环又会触发可读事件。
                     * 即一个可读事件，会被多个子线程处理，如果不加synchronized关键字，推测可能还会出现每个子线程各读取一部分buffer。
                     */
                    ByteBuffer buffer = ByteBuffer.allocate(256);
                    client.read(buffer);
                    executor.submit(new MultiServerRunnable(client, buffer));
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
}