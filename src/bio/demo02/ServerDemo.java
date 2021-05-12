package bio.demo02;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * 服务端 反复发送接收
 * @author zhangling 2021/5/12 13:24
 */
public class ServerDemo {
    public static void main(String[] args) throws IOException {
        System.out.println("==服务端启动==");
        ServerSocket serverSocket = new ServerSocket(8888);
        Socket socket = serverSocket.accept();
        InputStream is = socket.getInputStream();
        // 字节输入流 包装成 字符流读取
        BufferedReader br = new BufferedReader(new InputStreamReader(is));
        String line;
        while ((line = br.readLine()) != null) {
            System.out.println("服务端收到：" + line);
        }

    }
}
