package org.quickweb.utils;

import com.sun.istack.internal.Nullable;
import org.quickweb.exception.ParamEmptyException;
import org.quickweb.exception.ParamNotEqualsException;
import org.quickweb.exception.ParamNullException;

import java.util.Objects;

public class ParamUtils {

    public static <T> T requireNotNull(@Nullable T obj, String paramName) {
        ObjectUtils.requireNonNull(paramName);

        if (obj == null)
            throw new ParamNullException(paramName);
        return obj;
    }

    public static String requireNotEmpty(@Nullable String s, String paramName) {
        ObjectUtils.requireNonNull(paramName);

        if (s == null || s.isEmpty())
            throw new ParamEmptyException(paramName);
        return s;
    }

    public static void requireEquals(
            String name, @Nullable Object expectedValue, @Nullable Object actualValue) {
        ObjectUtils.requireNonNull(name);

        if (!Objects.equals(actualValue, expectedValue))
            throw new ParamNotEqualsException(name, expectedValue, actualValue);
    }
}
