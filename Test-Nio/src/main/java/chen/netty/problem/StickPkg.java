package chen.netty.problem;

import chen.netty.CommonClient;
import chen.netty.CommonServer;
import chen.netty.Const;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.LineBasedFrameDecoder;
import io.netty.handler.codec.string.StringDecoder;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 沾包拆包问题 , 可以使用
 * @see LineBasedFrameDecoder  换行分割
 * @see io.netty.handler.codec.DelimiterBasedFrameDecoder 自定义字符分割
 * @see io.netty.handler.codec.FixedLengthFrameDecoder 定长分割
 * @author chenwh
 * @date 2020/11/23
 */
@Slf4j
public class StickPkg {

    public static void main(String[] args) {
        CommonServer commonServer = new CommonServer(new LineBasedFrameDecoder(1024),new StringDecoder(),new ChannelInboundHandlerAdapter() {
            private  final AtomicInteger i = new AtomicInteger(0);

            @Override
            public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
                String receive = ((String) msg);
                if (receive.equals(Const.QUERY)) {
                    log.info("服务端接收到消息次数:{}", i.getAndIncrement());
                    ctx.writeAndFlush(Unpooled.copiedBuffer((Const.QUERY+System.getProperty("line.separator")).getBytes()));
                }
            }
        });

        CommonClient commonClient = new CommonClient(new LineBasedFrameDecoder(1024),new StringDecoder(),new ChannelInboundHandlerAdapter() {
            private  final AtomicInteger i = new AtomicInteger(0);

            @Override
            public void channelActive(ChannelHandlerContext ctx) throws Exception {
                for (int i1 = 0; i1 < 1000; i1++) {
                    ctx.writeAndFlush(Unpooled.copiedBuffer((Const.QUERY+System.getProperty("line.separator")).getBytes()));
                }
            }

            @Override
            public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
                String read = ((String) msg);
                if (Const.QUERY.equals(read)) {
                    log.info("客户端收到消息次数: {}", i.getAndIncrement());
                }
            }
        });

        CompletableFuture.runAsync(commonServer);
        CompletableFuture.runAsync(commonClient);
        while (true) {

        }
    }
}
