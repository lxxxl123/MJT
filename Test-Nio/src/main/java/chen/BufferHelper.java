package chen;

import utils.NioUtils;

import java.io.UnsupportedEncodingException;
import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.nio.channels.Channel;

/**
 * @author chenwh
 * @date 2020/11/11
 */
public class BufferHelper {

    /**
     * wrap 默认读模式
     * flip 切换到读模式
     * compact 切换回写模式 , 清理空间
     * get() 读完后自动变为写模式 , 但是空间变少
     * put() 在读模式写会把需要读的数据删除
     * remaining() 剩余可/写 limit - postion 读数据
     *  ByteBuffer教程
     */

    public static void main(String[] args) throws UnsupportedEncodingException {
        //分配空间
        ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
        //写
        byteBuffer.put("fuck you ".getBytes());
        //切换读 , 必须先切换在新建byte[]
        byteBuffer.flip();
        byte bytes[] = new byte[byteBuffer.remaining()];
        //读
        byteBuffer.get(bytes);
        System.out.println(new String(bytes, "utf-8"));

        System.out.println(byteBuffer.remaining());
        //切换到写
        byteBuffer.compact();
        System.out.println(byteBuffer.remaining());
    }
}
