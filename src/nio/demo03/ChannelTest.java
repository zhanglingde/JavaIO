package nio.demo03;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

/**
 * @author zhangling 2021/5/13 15:33
 */
public class ChannelTest {
    public static void main(String[] args) throws IOException {
        File srcFile = new File("C:\\Users\\Administrator.SC-202005261727\\Desktop\\picture.png");
        File destFile = new File("C:\\Users\\Administrator.SC-202005261727\\Desktop\\pictureNew.png");

        // 根据文件得到文件流
        FileInputStream is = new FileInputStream(srcFile);
        FileOutputStream os = new FileOutputStream(destFile);

        // 得到Channel
        FileChannel isChannel = is.getChannel();
        FileChannel osChannel = os.getChannel();

        ByteBuffer buffer = ByteBuffer.allocate(1024);
        while (true) {
            // 每次复制完 1024 字节后，清空buffer
            buffer.clear();
            // 从源文件读取1024字节到 buffer中
            int flag = isChannel.read(buffer);
            if (flag == -1) {
                break;
            }
            // limit = 1024,position = 0
            buffer.flip();
            // 将 buffer 中的数据写入到 osChannel中,传给文件输出流，写入文件中
            osChannel.write(buffer);
        }

        isChannel.close();
        osChannel.close();
        System.out.println("复制完成！");

    }
}
