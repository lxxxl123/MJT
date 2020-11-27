package chen.netty.problem.seriallize.msgpack;

import chen.netty.CommonClient;
import chen.netty.handler.EchoClientHandler;
import chen.netty.handler.MessagePackDecoder;
import chen.netty.handler.MessagePackEncoder;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.LengthFieldPrepender;

import java.util.concurrent.CompletableFuture;

/**
 * @author chenwh
 * @date 2020/11/27
 */
public class MsgpackClient {

    public static void main(String[] args) {
        CommonClient commonClient = new CommonClient(
                new LengthFieldBasedFrameDecoder(65535, 0, 2, 0, 2),
                new MessagePackDecoder(),
                //bytebuf前增加两个字节表示长度
                new LengthFieldPrepender(2),
                new MessagePackEncoder(),
                new EchoClientHandler()
        );

        CompletableFuture.runAsync(commonClient).join();

    }
}
