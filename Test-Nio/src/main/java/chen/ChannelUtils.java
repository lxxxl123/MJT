package chen;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.Channel;
import java.nio.channels.SelectableChannel;
import java.nio.channels.SocketChannel;

/**
 * @author chenwh
 * @date 2020/11/11
 */
public class ChannelUtils {
    public static void writeChannel( SocketChannel channel,String string,String charset) throws IOException {
        channel.write(ByteBuffer.wrap(string.getBytes(charset)));
    }

    public static String readChannel(SocketChannel channel, String charset) throws IOException {
        ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
        int read = channel.read(byteBuffer);
        if (read == -1) {
            return null;
        } else if (read == 0) {
            return "";
        }
        byteBuffer.flip();
        byte[] bytes = new byte[byteBuffer.remaining()];
        byteBuffer.get(bytes);
        return new String(bytes, charset);
    }
}
