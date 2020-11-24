package chen.netty.problem;

import chen.netty.CommonClient;
import chen.netty.CommonServer;
import chen.netty.Const;
import chen.utils.NettyBufUtils;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author chenwh
 * @date 2020/11/23
 */
@Slf4j
public class StickPkg {

    public static void main(String[] args) {
        CommonServer commonServer = new CommonServer(new ChannelInboundHandlerAdapter() {
            private  final AtomicInteger i = new AtomicInteger(0);

            @Override
            public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
                ByteBuf buf = ((ByteBuf) msg);
                byte[] bytes = new byte[buf.readableBytes()];
                buf.readBytes(bytes);
                String receive = new String(bytes, "utf-8");
                if (receive.equals(Const.QUERY)) {
                    log.info("服务端接收到消息次数:{}", i.getAndIncrement());
                    ctx.writeAndFlush(Unpooled.copiedBuffer(Const.QUERY.getBytes()));
                }
            }
        });

        CommonClient commonClient = new CommonClient(new ChannelInboundHandlerAdapter() {
            private  final AtomicInteger i = new AtomicInteger(0);

            @Override
            public void channelActive(ChannelHandlerContext ctx) throws Exception {
                for (int i1 = 0; i1 < 100; i1++) {
                    ctx.writeAndFlush(Unpooled.copiedBuffer(Const.QUERY.getBytes()));
                }
            }

            @Override
            public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
                String read = NettyBufUtils.read(msg);
                if (Const.QUERY.equals(read)) {
                    log.info("客户端收到消息次数: {}", i.getAndIncrement());
                }
                super.channelRead(ctx, msg);
            }
        });

        CompletableFuture.runAsync(commonServer);
        CompletableFuture.runAsync(commonClient);
        while (true) {

        }
    }
}
