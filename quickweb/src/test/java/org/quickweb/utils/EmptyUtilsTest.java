package org.quickweb.utils;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.Collection;

import static org.junit.Assert.*;

@RunWith(Parameterized.class)
public class EmptyUtilsTest {
    private Object[] param;
    private boolean expected;

    @Parameterized.Parameters
    public static Collection<Object[]> data() {
        Object[][] objects = new Object[][]{
                {null, true},
                {new Object[]{}, true},
                {new String[]{}, true},
                {new String[]{"a"}, false}
        };
        return Arrays.asList(objects);
    }

    public EmptyUtilsTest(Object[] param, boolean expected) {
        this.param = param;
        this.expected = expected;
    }

    @Test
    public void isEmpty() {
        assertEquals(expected, EmptyUtils.isEmpty(param));
    }
}