package org.quickweb.utils;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class StringUtilsTest {

    @Test
    public void isEmpty() {
        assertEquals(StringUtils.isEmpty(null), true);
        assertEquals(StringUtils.isEmpty(""), true);
        assertEquals(StringUtils.isEmpty("abc"), false);
        assertEquals(StringUtils.isEmpty("1"), false);
    }

    @Test
    public void isNotEmpty() {
        assertEquals(StringUtils.isNotEmpty(null), false);
        assertEquals(StringUtils.isNotEmpty(""), false);
        assertEquals(StringUtils.isNotEmpty("abc"), true);
        assertEquals(StringUtils.isNotEmpty("1"), true);
    }

    @Test
    public void joinStringArr() {
        String[] arr = null;
        assertEquals(StringUtils.join(arr, ','), null);
        arr = new String[]{};
        assertEquals(StringUtils.join(arr, ','), "");
        arr = new String[]{"aa"};
        assertEquals(StringUtils.join(arr, ','), "aa");
        arr = new String[]{"aa", "bb"};
        assertEquals(StringUtils.join(arr, ','), "aa,bb");
        arr = new String[]{"aa", "bb", "cc"};
        assertEquals(StringUtils.join(arr, ','), "aa,bb,cc");
    }

    @Test
    public void joinCharArr() {
        char[] arr = null;
        assertEquals(StringUtils.join(arr, ','), null);
        arr = new char[]{};
        assertEquals(StringUtils.join(arr, ','), "");
        arr = new char[]{'a'};
        assertEquals(StringUtils.join(arr, ','), "a");
        arr = new char[]{'a', 'b'};
        assertEquals(StringUtils.join(arr, ','), "a,b");
        arr = new char[]{'a', 'b', 'c'};
        assertEquals(StringUtils.join(arr, ','), "a,b,c");
    }

    @Test
    public void joinCollection() {
        List<String> list = null;
        assertEquals(StringUtils.join(list, ','), null);
        list = new ArrayList<>();
        assertEquals(StringUtils.join(list, ','), "");
        list.add("aa");
        assertEquals(StringUtils.join(list, ','), "aa");
        list.add("bb");
        assertEquals(StringUtils.join(list, ','), "aa,bb");
        list.add("cc");
        assertEquals(StringUtils.join(list, ','), "aa,bb,cc");
    }

    @Test
    public void joinIterator() {
        List<String> list = null;
        assertEquals(StringUtils.join(list, ','), null);
        list = new ArrayList<>();
        assertEquals(StringUtils.join(list.iterator(), ','), "");
        list.add("aa");
        assertEquals(StringUtils.join(list.iterator(), ','), "aa");
        list.add("bb");
        assertEquals(StringUtils.join(list.iterator(), ','), "aa,bb");
        list.add("cc");
        assertEquals(StringUtils.join(list.iterator(), ','), "aa,bb,cc");
    }

    @Test
    public void split() {
        assertArrayEquals(StringUtils.split(null, ' '), null);
        assertArrayEquals(StringUtils.split("", ' '), new String[]{});
        assertArrayEquals(StringUtils.split(",", ','), new String[]{});
        assertArrayEquals(StringUtils.split(",,", ','), new String[]{});
        assertArrayEquals(StringUtils.split("a", ' '), new String[]{"a"});
        assertArrayEquals(StringUtils.split("a b", ' '), new String[]{"a", "b"});
        assertArrayEquals(StringUtils.split("aa bb", ' '), new String[]{"aa", "bb"});
        assertArrayEquals(StringUtils.split("aa,bb", ','), new String[]{"aa", "bb"});
    }
}