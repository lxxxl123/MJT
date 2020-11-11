package chen.utils;


import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;


public class NioUtils {

    public static void writeChannel(SocketChannel channel, String string, String charset) throws IOException {
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
