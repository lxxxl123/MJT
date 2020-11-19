package chen.aio;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.nio.channels.AsynchronousServerSocketChannel;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CountDownLatch;

/**
 * @author chenwh
 * @date 2020/11/11
 */
@Slf4j
public class AioTimerServer implements Runnable {

    AsynchronousServerSocketChannel serverSocketChannel;

    CountDownLatch latch;

    @SneakyThrows
    @Override
    public void run() {
        try {
            serverSocketChannel = AsynchronousServerSocketChannel.open();
            serverSocketChannel.bind(new InetSocketAddress("localhost", 8877));
        } catch (IOException e) {
            log.error("", e);
        }
        latch = new CountDownLatch(1);
        doAccept();
        latch.await();
        log.info("服务端断开");
    }

    private void doAccept() throws Exception{
        serverSocketChannel.accept(this, new CompletionHandler<AsynchronousSocketChannel, AioTimerServer>() {
            //连接成功事件
            @Override
            public void completed(AsynchronousSocketChannel channel, AioTimerServer attachment) {
                attachment.serverSocketChannel.accept(attachment, this);
                log.info("连接成功");
                ByteBuffer buff = ByteBuffer.allocate(1024);
                //读消息成功事件
                channel.read(buff, buff, new CompletionHandler<Integer, ByteBuffer>() {
                    @Override
                    public void completed(Integer result, ByteBuffer buffer) {
                        buffer.flip();
                        byte[] bytes = new byte[buffer.remaining()];
                        buffer.get(bytes);
                        log.info("成功读取到消息 = [{}] ", new String(bytes, Charset.forName("utf-8")));

                        //保持读
                        ByteBuffer buff = ByteBuffer.allocate(1024);
                        channel.read(buff, buff,this);

                        String sendMsg = Calendar.getInstance().toInstant().toString();
                        ByteBuffer writeBuffer = ByteBuffer.allocate(sendMsg.getBytes().length);
                        writeBuffer.put(sendMsg.getBytes());
                        writeBuffer.flip();
                        //写成功事件
                        channel.write(writeBuffer, writeBuffer, new CompletionHandler<Integer, ByteBuffer>() {
                            @Override
                            public void completed(Integer result, ByteBuffer attachment) {
                                if (attachment.hasRemaining()) {
                                    channel.write(attachment, attachment, this);
                                    log.info("continues write");
                                }
                            }

                            @Override
                            public void failed(Throwable exc, ByteBuffer attachment) {
                                try {
                                    log.error("连接断开", exc);
                                    channel.close();
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                        });

                    }

                    @Override
                    public void failed(Throwable exc, ByteBuffer buffer) {
                        log.error("读取消息失败", exc);
                    }
                });

            }

            @Override
            public void failed(Throwable exc, AioTimerServer attachment) {
                log.error("连接断开",exc);
                attachment.latch.countDown();
            }
        });

    }


    public static void main(String[] args) {
        CompletableFuture.runAsync(new AioTimerServer()).join();
    }
}
