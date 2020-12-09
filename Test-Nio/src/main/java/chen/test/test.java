package chen.test;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

/**
 * @author chenwh
 * @date 2020/11/19
 */
@Slf4j
public class test {
    public static void main(String[] args) {
        CompletableFuture<Void> future = CompletableFuture.runAsync(() -> {
            while (!Thread.currentThread().isInterrupted()) {
                System.out.println(1);

            }
        });
        try {
            TimeUnit.SECONDS.sleep(2);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        future.cancel(true);

        while (true) {

        }
    }
}
