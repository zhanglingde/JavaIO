package bio.demo04;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * 使用线程池管理客户端连接的 Socket
 *
 * @author zhangling  2021/5/12 19:08
 */
public class ServerDemo {

    public static void main(String[] args) throws IOException {
        ServerSocket serverSocket = new ServerSocket(8888);

        /**
         * 创建一个线程池
         * 线程池中最大线程数 3， 所以同时只能有 3个客户端连接的Socket能被响应；队列数为 3，队列中能存放 3 个Socket客户端连接；
         * 当有客户端挂了线程空闲出来后，队列中的 Socket 能被空闲的线程处理
         */
        HandlerSocketThreadPool handlerSocketThreadPool = new HandlerSocketThreadPool(3, 3);

        while (true) {
            Socket socket = serverSocket.accept();
            System.out.println("有人上线了！");
            // 使用线程池管理中的线程 操作Socket
            handlerSocketThreadPool.execute(new ReaderClientRunnable(socket));
        }

    }

    static class ReaderClientRunnable implements Runnable{

        private Socket socket;

        public ReaderClientRunnable(Socket socket) {
            this.socket =socket;
        }

        @Override
        public void run() {
            try {
                // 获取输入流
                InputStream is = socket.getInputStream();
                // 转成一个缓冲字符流
                Reader fr = new InputStreamReader(is);
                BufferedReader br = new BufferedReader(fr);
                // 一行一行读取数据，readLine是阻塞式的
                String line = null;
                while ((line = br.readLine()) != null) {
                    System.out.println("服务端收到数据："+line);
                }
            } catch (IOException e) {
                System.out.println("有人下线了");
            }

        }
    }

}
