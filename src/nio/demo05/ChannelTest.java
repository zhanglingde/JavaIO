package nio.demo05;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;

/**
 * @author zhangling 2021/5/13 16:07
 */
public class ChannelTest {
    public static void main(String[] args) throws IOException {
        FileInputStream is = new FileInputStream("data01.txt");
        FileChannel isChannel = is.getChannel();

        FileOutputStream os = new FileOutputStream("data03.txt");
        FileChannel osChannel = os.getChannel();

        // 复制
        osChannel.transferFrom(isChannel, isChannel.position(), isChannel.size());
        isChannel.close();
        osChannel.close();
    }
}
