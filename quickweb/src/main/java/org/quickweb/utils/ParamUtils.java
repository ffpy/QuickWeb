package org.quickweb.utils;

import com.sun.istack.internal.Nullable;

import java.util.Objects;

public class ParamUtils {

    public static <T> T requireNotNull(@Nullable T obj, String paramName) {
        RequireUtils.requireNotNull(paramName);
        if (obj == null)
            ExceptionUtils.throwNullException(paramName);
        return obj;
    }

    public static String requireNotEmpty(@Nullable String s, String paramName) {
        RequireUtils.requireNotNull(paramName);
        if (s == null || s.isEmpty())
            ExceptionUtils.throwEmptyException(paramName);
        return s;
    }

    public static void requireEquals(String name, @Nullable Object expectedValue,
                                     @Nullable Object actualValue) {
        RequireUtils.requireNotNull(name);
        if (!Objects.equals(actualValue, expectedValue))
            ExceptionUtils.throwNotEqualsException(name, expectedValue, actualValue);
    }
}
