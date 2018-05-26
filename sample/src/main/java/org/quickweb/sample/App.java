package org.quickweb.sample;

import org.quickweb.session.param.ParamHelper;

public class App {
    public static void main(String[] args) {
//        String pattern = "\\$((\\w*):?([\\w_]*)([\\w_.]*))";
//        String s = "name = $x:name.aaa.bbb";
////        String s = "name = $x:name";
//        Pattern compile = Pattern.compile(pattern);
//        Matcher matcher = compile.matcher(s);
//        while (matcher.find()) {
//            for (int i = 0; i <= matcher.groupCount(); i++) {
//                System.out.println(i + ":" + matcher.group(i));
//            }
//        }

        String s = "x:name[2][1].aaa.bbb";
        ParamHelper helper = new ParamHelper(s);
        System.out.println(helper.getScopeName());
        System.out.println(helper.getName());
        System.out.println(helper.getMembers());
        while (helper.hasMember()) {
            System.out.println(helper.findMember());
        }
    }
}
