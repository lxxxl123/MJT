package chen.netty.problem.seriallize.msgpack;

import chen.netty.model.User;
import org.msgpack.MessagePack;
import org.msgpack.type.Value;

import java.io.IOException;

/**
 * @author chenwh
 * @date 2020/11/27
 */
public class Test {
    /**
     * @author chenwh
     * @date 2020/11/27
     */
    public static void main(String[] args) throws IOException {
        User user = new User("chen",1);

        MessagePack messagePack = new MessagePack();
        byte[] bytes = messagePack.write(user);
        Value read1 = messagePack.read(bytes);
        System.out.println(read1);



    }
}
