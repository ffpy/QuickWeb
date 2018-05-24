package org.quickweb.utils;

public class CharUtils {

    /**
     * 首字母大写
     */
    public static String upperCaseInitial(String s) {
        if (StringUtils.isEmpty(s))
            return s;

        char[] cs = s.toCharArray();
        if (cs[0] >= 'a' && cs[0] <= 'z')
            cs[0] -= 32;
        return String.valueOf(cs);
    }
}
