package chen.utils;


import lombok.extern.slf4j.Slf4j;

import java.util.*;
import java.util.stream.Collectors;

//运算工具
@Slf4j
public class OperUtils {

    private static final String AND = "and";

    private static final String OR = "or";

    private static final String _AND_ = " and ";

    private static final String _OR_ = " or ";

    private static class Tree {
        Tree(String stat){
            this.stat = stat;
        }

        Tree(){
            this.stat = OR;
        }
                
        String stat;


        List<Tree> sons = new ArrayList<>();

        private void addSons(StringBuilder s) {
            Tree tree = new Tree(s.toString());
            s.delete(0, s.length());
            sons.add(tree);

        }

        private void addSons(Tree t) {
            sons.add(t);
        }

        private static String statAndStat(String stat1,String stat2){
            LinkedList<StringBuilder> list = Arrays
                    .stream(stat1.split(OR))
                    .map(StringBuilder::new)
                    .collect(Collectors.toCollection(LinkedList::new));
            LinkedList<StringBuilder> newlist = new LinkedList<>();
            for (String s : stat2.split(OR)) {
                for (StringBuilder or : list) {
                    newlist.add(new StringBuilder(or.toString() + _AND_ + s));
                }
            }
            return String.join(_OR_,newlist);
        }

        @Override
        public String toString() {
            try {
                if (AND.equals(stat)) {
                    return sons.stream().map(Tree::toString).reduce(Tree::statAndStat).orElse("");
                } else if (OR.equals(stat)) {
                    return sons.stream().map(Tree::toString).collect(Collectors.joining(_OR_));
                } else {
                    return stat;
                }
            } catch (Exception e) {
                log.error("", e);
                throw new RuntimeException(e);
            }

        }

    }



    public static String splitBracket(String oper) {
        oper = oper.replace(AND, "&");
        oper = oper.replace(OR, "|");
        return createTree(oper).toString();
    }



    private static Tree createTree(String oper){
        char[] chars = oper.toCharArray();
        StringBuilder sb = new StringBuilder();
        Tree cur = new Tree();
        Tree root = cur;

        for (int i = 0; i < chars.length; i++) {
            if (chars[i] == '(') {
                int num = 1;
                int right = -1;
                int left = i+1;
                for (int j = i+1; j < chars.length; j++) {
                    if (chars[j] == '(') {
                        num++;
                    } else if (chars[j] == ')') {
                        num--;
                    }
                    if (num == 0) {
                        right = j;
                        break;
                    }
                }
                sb.append(createTree(oper.substring(left, right)));
                i = right+1;
            }
            else if (chars[i] == '&') {
                 if (AND.equals(cur.stat)) {
                    cur.addSons(sb);
                } else if (OR.equals(cur.stat) ) {
                    Tree son = new Tree(AND);
                    cur.addSons(son);
                    son.addSons(sb);
                    cur = son;
                }
            } else if (chars[i] == '|') {
                 if (AND.equals(cur.stat)) {
                    cur.addSons(sb);
                    cur = root;
                } else if (OR.equals(cur.stat)) {
                    cur.addSons(sb);
                }
            } else {
                sb.append(chars[i]);
            }
            if (i >= chars.length-1) {
                cur.addSons(sb);
            }
        }

        return root;
    }

    /**
     *  1
     *
     * @param args
     */


    public static void main(String[] args) {
        String s = " ( 1 or 2 and 7 ) and (3 or 4 )";

        System.out.println(splitBracket(s));

    }
}
