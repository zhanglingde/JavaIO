package bio.demo01;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.net.Socket;

/**
 * 客户端
 * @author zhangling 2021/5/12 11:36
 */
public class ClientDemo {
    public static void main(String[] args) throws IOException {
        // 1.创建一个 Socket 的通信管道，请求与服务端的端口连接
        Socket socket = new Socket("127.0.0.1",8888);
        // 2.从 Socket 管道中得到一个字节输出流
        OutputStream os = socket.getOutputStream();
        // 3.将字节输出流 包装成 打印流（可以快速写一行数据出去）
        PrintStream ps = new PrintStream(os);
        // 4.向服务端发送消息
        ps.println("你好，我是客户端！");
        ps.flush();

    }
}
