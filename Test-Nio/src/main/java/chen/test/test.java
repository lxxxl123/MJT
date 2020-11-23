package chen.test;

import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;

/**
 * @author chenwh
 * @date 2020/11/19
 */
@Slf4j
public class test {
    public static void main(String[] args) {
        HashMap<Integer,String> map = new HashMap<>();
        map.put(null, "a");
        System.out.println(map);
    }
}
