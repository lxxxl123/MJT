package chen.nio;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import utils.NioUtils;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Calendar;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.CompletableFuture;

/**
 * 用法:
 * 1.启动NioTest#main()
 * 2.cmd : telnet localhost 8877
 * 3.ctrl + ] 进入指令模式
 * 4.cmd : send [msg]
 * 5.cmd ENTER  -- 返回交互模式
 */
@Slf4j
public class TimerServer implements Runnable {

    private Selector selector;

    private ServerSocketChannel serverSocketChannel;

    private volatile boolean stop;

    public TimerServer(){
        try {
            selector = Selector.open();
            serverSocketChannel = ServerSocketChannel.open();
            //监听端口
            serverSocketChannel.socket().bind(new InetSocketAddress("127.0.0.1", 8877), 1024);
            //开启nio
            serverSocketChannel.configureBlocking(false);
            //注册需要监听的事件
            serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);

        } catch (Exception e) {
            log.error("",e);
        }
    }


    @Override
    public void run() {
        while (true) {

            try {
                selector.select(1000);
                Set<SelectionKey> selectionKeys = selector.selectedKeys();
                Iterator<SelectionKey> iterator = selectionKeys.iterator();
                SelectionKey selectionKey = null;
                while (iterator.hasNext()) {
                    selectionKey = iterator.next();
                    iterator.remove();
                    handleKey(selectionKey);
                }
            } catch (IOException e) {
                try {
                    selector.close();
                } catch (IOException ex) {
                    log.error("",ex);
                }
                log.error("",e);
            }

        }
    }

    private void handleKey(SelectionKey key) {
        if (key.isValid()) {
            try {
                if (key.isAcceptable()) {
                        ServerSocketChannel server = (ServerSocketChannel) key.channel();
                        SocketChannel sc = server.accept();
                        sc.configureBlocking(false);
                        sc.register(selector, SelectionKey.OP_READ);
                }
                if (key.isReadable()) {
                    SocketChannel channel = (SocketChannel) key.channel();
                    String read = null;
                    if(StringUtils.isNotEmpty(read = NioUtils.readChannel(channel, "utf-8"))){
                        log.info("接收到消息:{}", read);
                        String out = String.format("当日时间: %s\r\n", Calendar.getInstance().toInstant());
                        NioUtils.writeChannel(channel,out,"gbk");
                    }
                    if (read == null) {
                        log.info("客户端断开 {} {}",channel.getLocalAddress(),channel.getRemoteAddress());
                        key.cancel();
                        channel.close();
                    }
                }

            } catch (Exception e) {
                key.cancel();
                try {
                    key.channel().close();
                } catch (IOException ex) { }
                log.error("", e);
            }
        }
    }

    public static void main(String[] args) {
        CompletableFuture.runAsync(new TimerServer()).join();
    }
}
