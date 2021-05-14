package nio.demo06;

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
    public static void main(String[] args) throws IOException {
        // 1.获取通道
        ServerSocketChannel ssChannel = ServerSocketChannel.open();
        // 2.切换非阻塞模式
        ssChannel.bind(new InetSocketAddress(8888));
        // 3.绑定连接
        ssChannel.configureBlocking(false);

        // 4.获取选择器
        Selector selector = Selector.open();
        // 5.将通道注册到选择器上，并且指定监听 “接收事件”
        ssChannel.register(selector, SelectionKey.OP_ACCEPT);
        // 6.轮询获取Selector上监听到的事件
        int select;
        while ((select = selector.select()) > 0) {
            //int select = selector.select();
            System.out.println("select = " + select);
            // 7.获取当前Selector中所有注册的 Channel 通道中监听到的事件
            Set<SelectionKey> selectionKeys = selector.selectedKeys();
            Iterator<SelectionKey> it = selectionKeys.iterator();
            while (it.hasNext()) {
                SelectionKey sk = it.next();
                it.remove();
                handle(sk);
            }
        }
    }

    /**
     * 根据 SelectionKey 事件的类型，处理不同的事件
     *
     * @param key Channel 注册时的事件类型
     */
    private static void handle(SelectionKey key) {
        if (key.isAcceptable()) {  // 接收事件
            try {
                ServerSocketChannel ssChannel = (ServerSocketChannel) key.channel();
                // 获取客户端连接
                SocketChannel socketChannel = ssChannel.accept();
                socketChannel.configureBlocking(false);
                socketChannel.register(key.selector(), SelectionKey.OP_READ);
                System.out.println("客户端【"+socketChannel.getRemoteAddress()+"】上线了！");
            } catch (IOException e) {
                e.printStackTrace();
            }

        } else if (key.isReadable()) { // 读取事件
            try {
                // 获取 Channel
                SocketChannel channel = (SocketChannel) key.channel();
                // 获取Buffer缓冲区读取数据
                ByteBuffer buffer = ByteBuffer.allocate(1024);
                int len = 0;
                while ((len = channel.read(buffer)) > 0) {
                    // limit = len,position = 0
                    buffer.flip();
                    System.out.println(new String(buffer.array(), 0, len));
                    buffer.clear();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
