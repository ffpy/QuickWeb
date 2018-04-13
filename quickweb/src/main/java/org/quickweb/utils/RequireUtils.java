package org.quickweb.utils;

import org.apache.commons.lang3.StringUtils;

import java.util.Objects;

public class RequireUtils {

    public static void requireNotNull(Object... objects) {
        Objects.requireNonNull(objects);
        for (Object o : objects) {
            Objects.requireNonNull(o);
        }
    }

    public static void requireArrayNotNull(Object[] obj) {
        Objects.requireNonNull(obj);
    }

    public static void requireNotEmpty(String... ss) {
        Objects.requireNonNull(ss);
        for (String s : ss) {
            if (StringUtils.isEmpty(s))
                ExceptionUtils.throwEmptyException();
        }
    }

    public static void requireArrayNotEmpty(String[] ss) {
        if (EmptyUtils.isEmpty(ss))
            ExceptionUtils.throwEmptyException();
    }
}
