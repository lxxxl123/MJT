package chen.netty.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.msgpack.annotation.Message;

import java.io.Serializable;

/**
 * @author chenwh
 * @date 2020/11/27
 */
@Data
@Message
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class User implements Serializable {
    private static final long serialVersionUID = 1L;

    String name;
    Integer age;
}
