package chen.utils;

import io.netty.buffer.ByteBuf;

/**
 * @author chenwh
 * @date 2020/11/24
 */
public class NettyBufUtils {

    public static String read(Object msg) throws Exception{
        ByteBuf buf = ((ByteBuf) msg);
        byte[] bytes = new byte[buf.readableBytes()];
        buf.readBytes(bytes);
        return new String(bytes, "utf-8");
    }
}
