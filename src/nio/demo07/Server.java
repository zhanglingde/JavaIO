package nio.demo07;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.util.Iterator;
import java.util.Set;

/**
 * @author zhangling 2021/5/14 9:23
 */
public class Server {
    private ServerSocketChannel ssChannel;
    private Selector selector;

    /**
     * 构造 NIO 服务器
     */
    public Server() {
        try {
            // 1.获取通道
            ssChannel = ServerSocketChannel.open();
            // 2.切换非阻塞模式
            ssChannel.bind(new InetSocketAddress(8888));
            // 3.绑定连接
            ssChannel.configureBlocking(false);

            // 4.获取选择器
            selector = Selector.open();
            // 5.将通道注册到选择器上，并且指定监听 “接收事件”
            ssChannel.register(selector, SelectionKey.OP_ACCEPT);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public static void main(String[] args) throws IOException {
        Server server = new Server();
        server.listen();
    }

    /**
     * 监听客户端事件
     */
    public void listen() {
        try {
            // 6.轮询获取Selector上监听到的事件
            int select;
            while ((select = selector.select()) > 0) {
                //int select = selector.select();
                //System.out.println("select = " + select);
                // 7.获取当前Selector中所有注册的 Channel 通道中监听到的事件
                Set<SelectionKey> selectionKeys = selector.selectedKeys();
                Iterator<SelectionKey> it = selectionKeys.iterator();
                while (it.hasNext()) {
                    SelectionKey sk = it.next();
                    it.remove();
                    handle(sk);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    /**
     * 根据 SelectionKey 事件的类型，处理客户端不同的事件
     *
     * @param key Channel 注册时的事件类型
     */
    private void handle(SelectionKey key) {
        if (key.isAcceptable()) {  // 接收事件
            try {
                ServerSocketChannel ssChannel = (ServerSocketChannel) key.channel();
                // 获取客户端连接
                SocketChannel socketChannel = ssChannel.accept();
                socketChannel.configureBlocking(false);
                socketChannel.register(key.selector(), SelectionKey.OP_READ);
                System.out.println("客户端【" + socketChannel.getRemoteAddress() + "】上线了！");
            } catch (IOException e) {
                e.printStackTrace();
            }

        } else if (key.isReadable()) { // 读取事件
            SocketChannel channel = null;
            try {
                // 获取 Channel
                channel = (SocketChannel) key.channel();
                // 获取Buffer缓冲区读取数据
                ByteBuffer buffer = ByteBuffer.allocate(1024);
                int len = 0;
                while ((len = channel.read(buffer)) > 0) {
                    // limit = len,position = 0
                    buffer.flip();
                    String msg = new String(buffer.array(), 0, len);
                    System.out.println("读取客户端消息：" + msg);
                    sendOtherClients(msg, channel);
                    buffer.clear();
                }
            } catch (IOException e) {
                e.printStackTrace();

                try {
                    System.out.println("客户端【" + channel.getRemoteAddress() + "】离线了！");
                    // 取消注册
                    key.cancel();
                    // 关闭通道
                    channel.close();
                } catch (IOException ioException) {
                    ioException.printStackTrace();
                }
            }
        }
    }

    /**
     * 将客户端的消息发送给其他客户端
     *
     * @param msg     消息内容
     * @param channel 自己的Channel
     */
    private void sendOtherClients(String msg, SocketChannel channel) throws IOException {

        // 遍历所有注册到Selector上的SocketChannel
        for (SelectionKey key : selector.keys()) {
            Channel ch = key.channel();
            // 排除ServerSocketChannel 和 发送消息的客户端Channel
            if (ch instanceof SocketChannel && ch != channel) {
                SocketChannel clientChannel = (SocketChannel) ch;
                // 将消息存储到 buffer
                ByteBuffer buffer = ByteBuffer.wrap(msg.getBytes());
                // 将buffer 数据写入channel
                clientChannel.write(buffer);
            }

        }
    }
}
