package bio.portForward;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * @author zhangling  2021/5/12 21:32
 */
public class Server {
    public static void main(String[] args) {
        try {
            ServerSocket ss = new ServerSocket(8888);


            Socket socket = ss.accept();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
