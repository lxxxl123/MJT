import handler.SimpleHanlder;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

import java.net.InetSocketAddress;

/**
 * @author chenwh
 * @date 2020/11/9
 */
public class ServerMain {

    public static void main(String[] args) {
        int port = 8777;
        EventLoopGroup boss = new NioEventLoopGroup();
        EventLoopGroup work = new NioEventLoopGroup();
        SimpleHanlder simpleHanlder = new SimpleHanlder();
        ServerBootstrap server = new ServerBootstrap();
        server.group(boss,work)
                .channel(NioServerSocketChannel.class)
                .option(ChannelOption.SO_BACKLOG,1024)
                .localAddress(new InetSocketAddress("127.0.0.1",port))
                .childHandler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel ch) throws Exception {
                        ch.pipeline().addLast(simpleHanlder);
                    }
                });
        try {
            ChannelFuture f = server.bind().sync();
            f.channel().closeFuture().sync();
        } catch (Exception e) {
            boss.shutdownGracefully();
            work.shutdownGracefully();
        }

    }
}
