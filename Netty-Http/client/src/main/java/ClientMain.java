import handler.SimpleHandler;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

import java.net.InetSocketAddress;

/**
 * @author chenwh
 * @date 2020/11/9
 */
public class ClientMain {

    public static void main(String[] args) {
        int port = 8777;
        EventLoopGroup group = new NioEventLoopGroup();
        Bootstrap bootstrap = new Bootstrap();
        bootstrap.group(group)
                .channel(NioSocketChannel.class)
                .remoteAddress(new InetSocketAddress("127.0.0.1",port))
                .handler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel ch) throws Exception {
                        ch.pipeline().addLast(new SimpleHandler());
                    }
                });
        try {
            ChannelFuture channelFuture = bootstrap.connect().sync();
            channelFuture.channel().close().sync();
        } catch (InterruptedException e) {
            group.shutdownGracefully();
        }
    }
}
