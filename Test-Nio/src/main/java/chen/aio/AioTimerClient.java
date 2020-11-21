package chen.aio;

import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;
import java.util.concurrent.CompletableFuture;

/**
 * @author chenwh
 * @date 2020/11/17
 */
@Slf4j
public class AioTimerClient implements Runnable, CompletionHandler<Void, AioTimerClient> {

    AsynchronousSocketChannel socketChannel;

    @Override
    public void run() {
        try {
            socketChannel = AsynchronousSocketChannel.open();
        } catch (IOException e) {
            e.printStackTrace();
        }
        socketChannel.connect(new InetSocketAddress("localhost", 8877),this,this);
        while (true) {

        }
    }

    @Override
    public void completed(Void result, AioTimerClient attachment) {
        //连接成功触发
        log.info("连接到服务器");
        String msg = "请问服务端收到了吗?";
        ByteBuffer allocate = ByteBuffer.allocate(msg.getBytes().length);
        allocate.put(msg.getBytes());
        allocate.flip();
        socketChannel.write(allocate, allocate, new CompletionHandler<Integer, ByteBuffer>() {
            @Override
            public void completed(Integer result, ByteBuffer attachment) {
                //写入服务端触发
                if (attachment.hasRemaining()) {
                    socketChannel.write(attachment, attachment, this);
                }else{
                    //开始读
                    ByteBuffer allocate = ByteBuffer.allocate(1024);
                    socketChannel.read(allocate, allocate, new CompletionHandler<Integer, ByteBuffer>() {
                        @Override
                        public void completed(Integer result, ByteBuffer attachment) {
//                            ByteBuffer allocate = ByteBuffer.allocate(1024);
//                            //保持读
//                            socketChannel.read(allocate, allocate, this);

                            attachment.flip();
                            byte[] bytes = new byte[attachment.remaining()];
                            attachment.get(bytes);
                            log.info("成功读取服务端数据 = {}", new String(bytes));
                        }

                        @Override
                        public void failed(Throwable exc, ByteBuffer attachment) {
                            try {
                                log.error("",exc);
                                socketChannel.close();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    });
                }

            }

            @Override
            public void failed(Throwable exc, ByteBuffer attachment) {
                try {
                    log.error("",exc);
                    socketChannel.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        });
    }

    @Override
    public void failed(Throwable exc, AioTimerClient attachment) {
        try {
            log.error("",exc);
            socketChannel.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        CompletableFuture.runAsync(new AioTimerClient()).join();
    }
}
