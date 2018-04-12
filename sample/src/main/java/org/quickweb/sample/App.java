package org.quickweb.sample;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class App {
    public static void main(String[] args) {
        String s = "name = $name and id = $r:id";
        String pattern  = "\\$((\\w+):)?([\\w_]+)";

        Pattern compile = Pattern.compile(pattern);
        Matcher matcher = compile.matcher(s);
        while (matcher.find()) {
            for (int i = 0; i <= matcher.groupCount(); i++) {
                System.out.println(i + ": " + matcher.group(i));
            }
        }
    }
}
