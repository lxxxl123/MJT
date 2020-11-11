import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.awt.*;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.CompletableFuture;

/**
 * @author chenwh
 * @date 2020/11/11
 */
@Slf4j
public class TimerClient implements Runnable {


    private Selector selector;
    private SocketChannel socketChannel;


    public TimerClient(){
        try {
            selector = Selector.open();
            socketChannel = SocketChannel.open();
            socketChannel.configureBlocking(false);
            socketChannel.register(selector, SelectionKey.OP_CONNECT);

        } catch (IOException e) {
            log.error("",e);
        }

    }


    @Override
    public void run() {
        try {
            doConnect();
        } catch (IOException e) {
            log.error("", e);
        }
        while (true) {
            try {
                selector.select(1000);
                Set<SelectionKey> selectionKeys = selector.selectedKeys();
                Iterator<SelectionKey> iterator = selectionKeys.iterator();
                while (iterator.hasNext()) {
                    SelectionKey key = iterator.next();
                    handleKey(key);
                }
            } catch (Exception e) {
                try {
                    selector.close();
                } catch (IOException ex) { }
                log.error("",e);
                System.exit(0);
            }


        }
    }

    private void handleKey(SelectionKey key) throws Exception{
        if (key.isValid()) {
            SocketChannel channel = (SocketChannel) key.channel();
            if (key.isConnectable()) {
                if (channel.finishConnect()) {
                    ChannelUtils.writeChannel(channel, "客户端连接成功", "utf-8");
                    channel.register(selector, SelectionKey.OP_READ);
                }else {
                    channel.close();
                }
            }
            if (key.isReadable()) {
                String read = ChannelUtils.readChannel(channel, "utf-8");
                if (StringUtils.isNotEmpty(read)) {
                    log.info("读取到服务端消息 {}", read);
                }
                if (read == null) {
                    log.info("服务端断开");
                    key.cancel();
                    channel.close();
                }
            }



        }
    }

    private void doConnect() throws IOException {
        boolean connected = socketChannel.connect(new InetSocketAddress("localhost", 8877));
        if (connected) {
            socketChannel.register(selector, SelectionKey.OP_READ);
        }else {
            socketChannel.register(selector, SelectionKey.OP_CONNECT);
        }
    }

    public static void main(String[] args) {
        CompletableFuture.runAsync(new TimerClient()).join();

    }
}
