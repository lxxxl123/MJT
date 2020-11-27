package chen.netty.handler;

import chen.netty.model.User;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import lombok.extern.slf4j.Slf4j;

/**
 * @author chenwh
 * @date 2020/11/27
 */
@Slf4j
public class EchoClientHandler extends ChannelInboundHandlerAdapter {
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        for (int i = 0; i < 10; i++) {
            User chen = new User("chen", 1);
            ctx.write(chen);
            log.info("Client:发送消息{}", chen);
        }
        ctx.flush();
        super.channelActive(ctx);
    }



    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        log.error("",cause);
    }
}
