package org.quickweb.utils;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.Collection;

import static org.junit.Assert.assertEquals;

@RunWith(Parameterized.class)
public class CharUtilsTest {
    private String param;
    private String expected;

    @Parameterized.Parameters
    public static Collection<Object[]> data() {
        Object[][] objects = new Object[][]{
                {null, null},
                {"", ""},
                {"a", "A"},
                {"ab", "Ab"},
                {"A", "A"},
                {"AB", "AB"},
        };
        return Arrays.asList(objects);
    }

    public CharUtilsTest(String param, String expected) {
        this.param = param;
        this.expected = expected;
    }

    @Test
    public void upperCaseInitial() {
        assertEquals(expected, CharUtils.upperCaseInitial(param));
    }
}