package org.quickweb.sample;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class App {
    public static void main(String[] args) {
        String s = "name = $name and id = $id";
        String pattern  = "\\$([\\w_]*)";

        s = s.replaceAll(pattern, "?");
        System.out.println(s);
//        Pattern compile = Pattern.compile(pattern);
//        Matcher matcher = compile.matcher(s);
//        while (matcher.find()) {
//            System.out.println(matcher.group(1));
//        }
    }
}
