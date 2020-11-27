package chen.test;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.TimeUnit;
import java.util.stream.Stream;

/**
 * @author chenwh
 * @date 2020/11/19
 */
@Slf4j
public class test {
    public static void main(String[] args) {
        Stream.of("abcd","abcd").parallel().forEach(e-> {
            synchronized ((e + "a").intern()) {
                System.out.println("字符串");
                try {
                    TimeUnit.SECONDS.sleep(5);
                } catch (InterruptedException interruptedException) {
                    interruptedException.printStackTrace();
                }
            }
        });
    }
}
