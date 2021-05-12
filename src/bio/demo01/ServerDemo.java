package bio.demo01;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * 服务端
 * @author zhangling 2021/5/12 11:29
 */
public class ServerDemo {
    public static void main(String[] args) throws IOException {
        System.out.println("==启动服务端==");
        // 1.注册端口
        ServerSocket serverSocket = new ServerSocket(8888);
        // 2.阻塞等待客户端连接，得到一个端到端的 Socket 管道
        Socket socket = serverSocket.accept();
        // 3.从 Socket 管道中得到一个客户端发送来的字节输入流
        InputStream is = socket.getInputStream();
        // 4.将字节输入流包装成自己需要的流进行数据的读取
        BufferedReader br = new BufferedReader(new InputStreamReader(is));
        String line;
        // 等待读取一行数据，客户端发送的数据需要有换行符
        while ((line = br.readLine()) != null) {
            // 读取一行消息后，下一次循环等待客户端下一次发送消息，此时客户端发送完消息后挂了，Socket通道关闭，服务端的Socket也会出现异常
            System.out.println("服务端收到：" + line);
        }
    }
}
