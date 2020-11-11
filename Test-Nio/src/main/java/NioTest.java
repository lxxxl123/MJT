import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sun.rmi.runtime.Log;

import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.util.Calendar;
import java.util.concurrent.CompletableFuture;

/**
 * @author chenwh
 * @date 2020/11/11
 */
@Data
@Slf4j
public class NioTest {

    public static void main(String[] args) {
        CompletableFuture<Void> future = CompletableFuture.runAsync(new TimerServer());
        future.join();
//        System.out.println(Calendar.getInstance().toInstant());
    }
}
