package nio.demo07;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectableChannel;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Scanner;

/**
 * @author zhangling 2021/5/14 10:38
 */
public class Client {

    private Selector selector;
    private SocketChannel socketChannel;
    private String username;

    private final String host = "127.0.0.1";
    private final Integer port = 8888;


    public Client() {
        try {
            selector = selector.open();
            socketChannel = socketChannel.open(new InetSocketAddress(host, port));
            socketChannel.configureBlocking(false);
            socketChannel.register(selector, SelectionKey.OP_READ);

            username = socketChannel.getLocalAddress().toString().substring(1);
            System.out.println("客户端用户名【" + username + "】");
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    /**
     * 向服务端发送消息
     */
    public void sendMsg(String msg) {
        msg = username + ":" + msg;
        try {
            // 将消息存入 Buffer 并 写入channel中
            socketChannel.write(ByteBuffer.wrap(msg.getBytes()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 接收消息
     */
    public void receiptMsg() {
        try {
            if (selector.select() > 0) {
                Iterator<SelectionKey> it = selector.selectedKeys().iterator();
                while (it.hasNext()) {
                    SelectionKey key = it.next();
                    it.remove();
                    if (key.isReadable()) {
                        SocketChannel channel = (SocketChannel) key.channel();
                        ByteBuffer buffer = ByteBuffer.allocate(1024);
                        // 将channel的数据读取到buffer 中
                        channel.read(buffer);
                        String msg = new String(buffer.array());
                        System.out.println(msg.trim());
                    }
                }
            }else{
                System.out.println("没有可用的Channel");
            }
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("远程主机端开");
            try {
                socketChannel.close();
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
        }
    }

    public static void main(String[] args) {
        Client client = new Client();

        // 创建一个新线程，每3s接收一次服务器的消息
        new Thread(()->{
            while (true) {
                client.receiptMsg();
                try {
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();

        Scanner sc = new Scanner(System.in);
        while (true) {
            System.out.print("请说：");
            String msg = sc.nextLine();
            client.sendMsg(msg);
        }
    }
}
