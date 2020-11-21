package org.example.chen.http;


import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class ResponseResult<T> {
    private int code;
    private T data;

    public static <E> ResponseResult<E> success(E data){
        return ((ResponseResult<E>) ResponseResult.builder().data(data).code(0).build());
    }

    public static <E> ResponseResult<E> fail(E data){
        return ((ResponseResult<E>) ResponseResult.builder().data(data).code(200).build());
    }
}
