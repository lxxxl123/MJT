package chen.netty;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import lombok.extern.slf4j.Slf4j;

import java.net.InetSocketAddress;
import java.util.Calendar;
import java.util.concurrent.CompletableFuture;

import static chen.netty.Const.QUERY;

/**
 * @author chenwh
 * @date 2020/11/17
 */
@Slf4j
public class CommonServer implements Runnable {

    @Override
    public void run() {
        EventLoopGroup boss = new NioEventLoopGroup();
        EventLoopGroup worker = new NioEventLoopGroup();
        ServerBootstrap server = new ServerBootstrap();
        server.group(boss, worker)
                .channel(NioServerSocketChannel.class)
                .option(ChannelOption.SO_BACKLOG, 1024)
                .childHandler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel ch) throws Exception {
                        ch.pipeline().addLast(new TimerHandler());
                    }
                });

        try {
            ChannelFuture future = server.bind(new InetSocketAddress("localhost", 8877)).sync();
            future.channel().closeFuture().sync();
        } catch (Exception e){
            log.error("", e);
        } finally {
            boss.shutdownGracefully();
            worker.shutdownGracefully();
        }


    }

    private static class TimerHandler extends ChannelInboundHandlerAdapter {
        @Override
        public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
            ByteBuf buf = (ByteBuf) msg;
            byte [] bytes = new byte[buf.readableBytes()];
            buf.readBytes(bytes);
            String receive = new String(bytes);
            log.info("接收到消息 = [{}]", receive);
            if (receive.equals(QUERY)) {
                ByteBuf byteBuf = Unpooled.copiedBuffer(Calendar.getInstance().toInstant().toString().getBytes());
                ctx.write(byteBuf);
            }
        }

        @Override
        public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
             ctx.flush();
        }

        @Override
        public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
            log.error("",cause);
            ctx.close();
        }
    }

    public static void main(String[] args) {
        CompletableFuture.runAsync(new CommonServer()).join();


    }
}
