package bio.portForward;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.net.Socket;
import java.util.Scanner;

/**
 * 客户端
 * @author zhangling  2021/5/12 21:29
 */
public class Client {
    public static void main(String[] args) throws IOException {
        Socket socket = new Socket("127.0.0.1", 8888);
        OutputStream os = socket.getOutputStream();
        // 输入流 包装成 打印流
        PrintStream ps = new PrintStream(os);
        Scanner sc = new Scanner(System.in);
        while (true) {
            System.out.print("请说：");
            String msg = sc.nextLine();
            ps.println(msg);
            ps.flush();
        }
    }
}
