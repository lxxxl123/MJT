package chen.netty.problem.seriallize.protobuf;

import com.google.protobuf.InvalidProtocolBufferException;

/**
 * @author chenwh
 * @date 2020/11/27
 */
public class Test {
    public static Student.StudentProto  decode(byte[] data) throws InvalidProtocolBufferException {
         return Student.StudentProto.parseFrom(data);
    }


    public static byte[]  encode(Student.StudentProto studentProto)  {
        return studentProto.toByteArray();
    }

    public static void main(String[] args) throws InvalidProtocolBufferException {
        Student.StudentProto chen = Student.StudentProto.newBuilder().setName("chen").setAge(1).build();
        byte[] encode = encode(chen);
        Student.StudentProto decode = decode(encode);
        System.out.println(decode);

    }
}
