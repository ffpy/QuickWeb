package org.quickweb.utils;

import java.util.Objects;

public class ObjectUtils {

    public static void requireNonNull(Object... objects) {
        Objects.requireNonNull(objects);
        for (Object o : objects) {
            Objects.requireNonNull(o);
        }
    }
}
