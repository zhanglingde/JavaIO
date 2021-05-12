package bio.sendfile;

import java.io.DataInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.UUID;

/**
 * 服务端：可以接收客户端的任意类型文件，并保存到服务端磁盘
 * @author zhangling  2021/5/12 20:28
 */
public class ServerDemo {
    public static void main(String[] args) {
        try {
            ServerSocket ss = new ServerSocket(8888);
            while (true) {
                Socket socket = ss.accept();

                new Thread(()->{
                    handle(socket);
                }).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 处理客户端的文件通信请求
     * @param socket
     */
    private static void handle(Socket socket) {
        try {
            // 1.得到一个数据输入流读取客户端发送过来的数据
            DataInputStream dis = new DataInputStream(socket.getInputStream());
            // 2.读取客户端发送过来的文件类型
            String suffix = dis.readUTF();
            System.out.println("服务端已经成功接收到了文件类型 = " + suffix);

            // 3.定义一个字节输出管道把客户端发送过来的文件数据写出去
            FileOutputStream os = new FileOutputStream("D:\\" + UUID.randomUUID().toString() + suffix);
            // 4.从数据输入流中读取文件数据，写出到字节输出流中去
            byte[] buffer = new byte[1024];
            int len;
            while ((len = dis.read(buffer)) > 0) {
                os.write(buffer, 0, len);
            }
            os.close();
            System.out.println("服务端接收文件保存成功！");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
