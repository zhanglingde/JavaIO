package bio.sendfile;

import java.io.*;
import java.net.Socket;

/**
 * @author zhangling  2021/5/12 20:37
 */
public class ClientDemo {
    public static void main(String[] args) {
        try (InputStream is = new FileInputStream("C:\\Users\\ling\\Desktop\\wallhaven-6od3px.jpg")){
            // 1.请求与服务端的 Socket 连接
            Socket socket = new Socket("127.0.0.1", 8888);
            // 2.把字节输出流包装成一个数据输出流
            DataOutputStream dos = new DataOutputStream(socket.getOutputStream());
            // 3.先发送上传文件的后缀给服务端
            dos.writeUTF(".jpg");
            // 4.把文件数据发送给服务端进行接收
            byte[] buffer = new byte[1024];
            int len;
            while ((len = is.read(buffer)) > 0) {
                dos.write(buffer,0,len);
            }
            dos.flush();
            Thread.sleep(10000);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
