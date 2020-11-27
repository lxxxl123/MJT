package chen.netty.problem.seriallize.protobuf;

import chen.netty.CommonServer;

import java.util.concurrent.CompletableFuture;

/**
 * @author chenwh
 * @date 2020/11/27
 */

public class ProtoServer {
    public static void main(String[] args) {
        CommonServer commonServer = new CommonServer();
        CompletableFuture.runAsync(commonServer).join();

    }
}
