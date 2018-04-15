package org.quickweb.utils;

import com.sun.istack.internal.Nullable;

import java.util.List;

/**
 * 空检查工具类
 */
public class EmptyUtils {

    public static boolean isEmpty(@Nullable List list) {
        return list == null || list.isEmpty();
    }

    public static boolean isEmpty(@Nullable Object[] objc) {
        return objc == null || objc.length == 0;
    }
}
