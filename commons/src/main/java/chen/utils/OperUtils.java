package chen.utils;


import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

//运算工具
@Slf4j
public class OperUtils {

    private static final String AND = "and";

    private static final String OR = "or";

    private static final String _AND_ = " and ";

    private static final String _OR_ = " or ";

    private static class Tree {


        private final String[] splitOr;

        private final String stat;


        Tree(String stat){
            if (!AND.equals(stat)) {
                this.splitOr = stat.split(_OR_);
            }else{
                this.splitOr = null;
            }
            this.stat = stat;

        }


        Tree(){
            this.stat = OR;
            this.splitOr = null;
        }

        private List<Tree> sons = new ArrayList<>();

        private void addSons(StringBuilder s) {
            Tree tree = new Tree(s.toString());
            s.delete(0, s.length());
            sons.add(tree);

        }

        private void addSons(Tree t) {
            sons.add(t);
        }

        private static String[] statAndStat(String[] split1,String[] split2){
            ArrayList<String> newlist = new ArrayList<>(split1.length*split2.length);
            for (String s1 : split1) {
                for (String s2 : split2) {
                    newlist.add(String.format("%s%s%s", s1, _AND_, s2));
                }
            }
            return newlist.toArray(new String[]{});
        }

        @Override
        public String toString() {
            try {
                if (AND.equals(stat)) {
                    return String.join(_OR_,sons.stream().map(e->e.splitOr).reduce(Tree::statAndStat).get());
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

        /**
         * 0 - 这个状态下遇到()会把它当成字符串
         * 1 - 这个状态下遇到( 会尝试拆分
         */
        int state = 1;
        for (int i = 0; i < chars.length; i++) {
            if (chars[i] == '(' && state == 1) {
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
                state = 1;
                 if (AND.equals(cur.stat)) {
                    cur.addSons(sb);
                } else if (OR.equals(cur.stat) ) {
                    Tree son = new Tree(AND);
                    cur.addSons(son);
                    son.addSons(sb);
                    cur = son;
                }
            } else if (chars[i] == '|') {
                state = 1;
                 if (AND.equals(cur.stat)) {
                    cur.addSons(sb);
                    cur = root;
                } else if (OR.equals(cur.stat)) {
                    cur.addSons(sb);
                }
            } else {
                if (chars[i] != ' ') {
                    state = 0;
                }
                sb.append(chars[i]);
            }
            if (i >= chars.length-1) {
                cur.addSons(sb);
            }
        }

        return root;
    }

}
