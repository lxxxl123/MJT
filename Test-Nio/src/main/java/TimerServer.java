import com.sun.org.apache.bcel.internal.generic.Select;
import lombok.extern.slf4j.Slf4j;

import javax.swing.*;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.nio.channels.spi.AbstractSelectionKey;
import java.util.Calendar;
import java.util.Iterator;
import java.util.Set;

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

    public static void main(String[] args) {

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
                e.printStackTrace();
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
                    ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
                    int read = -1;
                    while ((read = channel.read(byteBuffer)) > 0) {
                        byteBuffer.flip();
                        byte[] bytes = new byte[byteBuffer.remaining()];
                        byteBuffer.get(bytes);
                        log.info("接收到消息{}", new String(bytes, "utf-8"));
                        String out = String.format("当日时间: %s\r\n", Calendar.getInstance().toInstant());
                        channel.write(ByteBuffer.wrap(out.getBytes()));
                    }
                    if (read < 0) {
                        log.info("客户端断开 {} {}",channel.getLocalAddress(),channel.getRemoteAddress());
                        key.cancel();
                        channel.close();
                    }
                }

            } catch (IOException e) {
                log.error("", e);
            }
        }
    }
}
