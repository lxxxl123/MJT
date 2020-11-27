package chen.netty.problem.seriallize.protobuf;

import chen.netty.CommonClient;

import java.util.concurrent.CompletableFuture;

/**
 * @author chenwh
 * @date 2020/11/27
 */

public class ProtoClient {
    public static void main(String[] args) {
        CommonClient commonServer = new CommonClient(


        );
        CompletableFuture.runAsync(commonServer).join();

    }
}
