package chen.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

public class TreeUtils {
    /**
     * 返回大致结构 map<?,map<?,map<?,list>> , 作用同Collectors.groupBy
     */
     public static <T> Map getMapTree(List<T> list, Function<T, ?>... functions) {
        Map head = new HashMap<>();
        for (T t : list) {
            Map<Object,Object> cur = head;
            for (int i = 0; i < functions.length; i++) {
                Object key = functions[i].apply(t);
                if (i != functions.length - 1) {
                    cur.putIfAbsent(key, new HashMap<>());
                    cur = ((Map) cur.get(key));
                }else{
                    cur.putIfAbsent(key, new ArrayList<>());
                    ((List) cur.get(key)).add(t);
                }
            }

        }
        return head;
    }


}
