package chen.netty.problem.seriallize.protobuf;

import chen.netty.CommonServer;
import chen.netty.handler.StudentServerHandler;
import io.netty.handler.codec.protobuf.ProtobufDecoder;
import io.netty.handler.codec.protobuf.ProtobufEncoder;
import io.netty.handler.codec.protobuf.ProtobufVarint32FrameDecoder;
import io.netty.handler.codec.protobuf.ProtobufVarint32LengthFieldPrepender;

import java.util.concurrent.CompletableFuture;

/**
 * @author chenwh
 * @date 2020/11/27
 */

public class ProtoServer {
    public static void main(String[] args) {
        CommonServer commonServer = new CommonServer(
                new ProtobufVarint32FrameDecoder(),
                new ProtobufDecoder(Student.StudentProto.getDefaultInstance()),
                new ProtobufVarint32LengthFieldPrepender(),
                new ProtobufEncoder(),
                new StudentServerHandler()
        );
        CompletableFuture.runAsync(commonServer).join();

    }
}
