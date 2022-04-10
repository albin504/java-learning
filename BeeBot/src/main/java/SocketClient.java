import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

public class SocketClient {
    public static void main(String[] args) throws IOException {
        if (args.length != 2) {
            System.err.println(
                    "Usage: java EchoClient <host name> <port number>");
            System.exit(1);
        }

        String hostName = args[0];
        int portNumber = Integer.parseInt(args[1]);

        try (
                Socket socket = new Socket(hostName, portNumber);
                PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
                BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        ) {
            BufferedReader stdIn = new BufferedReader(new InputStreamReader(System.in));
            String serverResTxt;
            String userInputTxt;
            long inputTime = 0;
            while ((serverResTxt = in.readLine()) != null) {
                long duration = 0;
                if (inputTime > 0) {
                    duration = System.currentTimeMillis() - inputTime;
                }
                System.out.println("server:" + serverResTxt + (duration >0 ? "（耗时" + duration + "毫秒）" : ""));
                userInputTxt = stdIn.readLine();
                if (userInputTxt != null) {
                    inputTime = System.currentTimeMillis();
                    out.println(userInputTxt);
                }
            }
        } catch (UnknownHostException e) {
            System.err.println("Don't know about host " + hostName);
            System.exit(1);
        } catch (IOException e) {
            System.err.println("Couldn't get I/O for the connection to " +
                    hostName);
            System.exit(1);
        }
    }
}
