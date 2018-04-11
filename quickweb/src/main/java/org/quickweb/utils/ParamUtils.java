package org.quickweb.utils;

import org.quickweb.exception.ParamEmptyException;
import org.quickweb.exception.ParamNotEqualsException;
import org.quickweb.exception.ParamNullException;

import java.util.Objects;

public class ParamUtils {

    public static <T> T requireNotNull(T obj, String paramName) {
        if (obj == null)
            throw new ParamNullException(paramName);
        return obj;
    }

    public static String requireNotEmpty(String s, String paramName) {
        if (s == null || s.isEmpty())
            throw new ParamEmptyException(paramName);
        return s;
    }

    public static void requireEquals(String name, Object expectedValue, Object actualValue) {
        if (!Objects.equals(actualValue, expectedValue))
            throw new ParamNotEqualsException(name, expectedValue, actualValue);
    }
}
