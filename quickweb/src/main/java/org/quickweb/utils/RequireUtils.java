package org.quickweb.utils;

import org.quickweb.exception.ParamEmptyException;

import java.util.Objects;

public class ObjectUtils {

    public static void requireNotNull(Object... objects) {
        Objects.requireNonNull(objects);
        for (Object o : objects) {
            Objects.requireNonNull(o);
        }
    }

    public static void requireCollectionNotEmpty(String[] ss) {
        Objects.requireNonNull(ss);
        if (EmptyUtils.isEmpty(ss))
            throw new ParamEmptyException();
    }
}
