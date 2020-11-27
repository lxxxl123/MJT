package chen.netty.problem.seriallize.msgpack;

import chen.netty.CommonServer;
import chen.netty.handler.EchoServerHandler;
import chen.netty.handler.MessagePackDecoder;
import chen.netty.handler.MessagePackEncoder;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.LengthFieldPrepender;

import java.util.concurrent.CompletableFuture;

/**
 * @author chenwh
 * @date 2020/11/27
 */
public class MsgpackServer {

    public static void main(String[] args) {
        CommonServer commonServer = new CommonServer(
                new LengthFieldBasedFrameDecoder(65535, 0, 2, 0, 2),
                new MessagePackDecoder(),
                //bytebuf前增加两个字节表示长度
                new LengthFieldPrepender(2),
                new MessagePackEncoder(),
                new EchoServerHandler()
        );
        CompletableFuture.runAsync(commonServer).join();


    }
}
