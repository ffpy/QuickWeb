package org.quickweb.utils;

import java.util.*;

public class StringUtils {
    private static final String EMPTY = "";

    public static boolean isEmpty(CharSequence cs) {
        return cs == null || cs.length() == 0;
    }

    public static boolean isNotEmpty(CharSequence cs) {
        return !isEmpty(cs);
    }

    @SuppressWarnings("Duplicates")
    public static String join(final String[] array, final char separator) {
        if (array == null) {
            return null;
        }
        if (array.length <= 0) {
            return EMPTY;
        }
        final StringBuilder buf = new StringBuilder(array.length * 16);
        for (int i = 0; i < array.length; i++) {
            if (i > 0) {
                buf.append(separator);
            }
            buf.append(array[i]);
        }
        return buf.toString();
    }

    @SuppressWarnings("Duplicates")
    public static String join(final char[] array, final char separator) {
        if (array == null) {
            return null;
        }
        if (array.length <= 0) {
            return EMPTY;
        }
        final StringBuilder buf = new StringBuilder(array.length * 16);
        for (int i = 0; i < array.length; i++) {
            if (i > 0) {
                buf.append(separator);
            }
            buf.append(array[i]);
        }
        return buf.toString();
    }

    public static String join(final Collection<?> collection, final char separator) {
        if (collection == null) {
            return null;
        }
        return join(collection.iterator(), separator);
    }

    public static String join(final Iterator<?> iterator, final char separator) {

        // handle null, zero and one elements before building a buffer
        if (iterator == null) {
            return null;
        }
        if (!iterator.hasNext()) {
            return EMPTY;
        }
        final Object first = iterator.next();
        if (!iterator.hasNext()) {
            return Objects.toString(first, "");
        }

        // two or more elements
        final StringBuilder buf = new StringBuilder(256); // Java default is 16, probably too small
        if (first != null) {
            buf.append(first);
        }

        while (iterator.hasNext()) {
            buf.append(separator);
            final Object obj = iterator.next();
            if (obj != null) {
                buf.append(obj);
            }
        }

        return buf.toString();
    }

    public static String[] split(final String str, final char separatorChar) {
        if (str == null) {
            return null;
        }
        final int len = str.length();
        if (len == 0) {
            return new String[0];
        }
        final List<String> list = new ArrayList<>();
        int i = 0, start = 0;
        boolean match = false;
        while (i < len) {
            if (str.charAt(i) == separatorChar) {
                if (match) {
                    list.add(str.substring(start, i));
                    match = false;
                }
                start = ++i;
                continue;
            }
            match = true;
            i++;
        }
        if (match) {
            list.add(str.substring(start, i));
        }
        return list.toArray(new String[list.size()]);
    }
}
