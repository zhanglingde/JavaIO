package nio.demo06;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.time.LocalTime;
import java.util.Scanner;

/**
 * @author zhangling 2021/5/14 9:44
 */
public class Client {
    public static void main(String[] args) throws Exception {
        // 1.获取通道
        SocketChannel channel = SocketChannel.open(new InetSocketAddress("127.0.0.1",8888));
        // 2.切换非阻塞模式
        channel.configureBlocking(false);
        // 3.分配指定大小的缓冲区
        ByteBuffer buffer = ByteBuffer.allocate(1024);
        Scanner sc = new Scanner(System.in);
        while (true) {
            System.out.print("请说：");
            String str = sc.nextLine();
            // 将数据写入buffer 缓冲区中
            buffer.put((LocalTime.now().toString() +"-"+ str).getBytes());

            buffer.flip();
            // 将buffer 的数据写入Channel
            channel.write(buffer);
            buffer.clear();

        }
        // 5. 关闭Channel
        //channel.close();
    }
}
