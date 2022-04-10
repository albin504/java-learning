import com.aliyuncs.exceptions.ClientException;
import org.apache.commons.configuration.ConfigurationException;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.Arrays;

public class MultiServerRunnable extends Thread {
    private SocketChannel socket;
    private ByteBuffer buffer;

    public MultiServerRunnable(SocketChannel socket, ByteBuffer buffer) {
        super("MultiServerThread");
        this.socket = socket;
        this.buffer = buffer;
    }

    public void run() {
        if (buffer.position() == 0) {
            return;
        }
        try {
            String userInput = null;
            BeeRot beeRot = BeeRot.getBeeBot(socket.getRemoteAddress().toString());
            // 获取用户输入
            if (buffer.hasArray()) {
                buffer.flip();
                try {
                    userInput = new String(trim(buffer.array()), "UTF-8");
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                userInput = userInput.trim();
                String responseTxt = null;
                // 机器人作出相应
                responseTxt = beeRot.dealRequest(userInput);
                responseTxt = responseTxt + "\n";
                ByteBuffer responseBuffer = null;
                responseBuffer = ByteBuffer.wrap(responseTxt.getBytes("UTF-8"));
                socket.write(responseBuffer);
            } else {
                ByteBuffer buffer1 = ByteBuffer.wrap("无效输入？\n".getBytes("UTF-8"));
                socket.write(buffer1);
            }
        } catch (IOException | InterruptedException | ClientException | ConfigurationException e) {
            e.printStackTrace();
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