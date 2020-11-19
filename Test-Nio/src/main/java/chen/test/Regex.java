package chen.test;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author chenwh
 * @date 2020/11/19
 */
public class Regex {
    final static Pattern pattern = Pattern.compile("accountName[+\\s](like|=)[+\\s]\\*(.+?)\\*,?");

    public static void main(String[] args) {
        Matcher matcher = pattern.matcher("213213 accountName like *view*,");
        while (matcher.find()) {
            System.out.println(matcher.group(2));
        }

        System.out.println("a"+null+"a");
    }
}
