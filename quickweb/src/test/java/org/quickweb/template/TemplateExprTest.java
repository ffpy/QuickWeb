package org.quickweb.template;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.quickweb.session.QuickSession;

import java.util.*;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

@RunWith(Parameterized.class)
public class TemplateExprTest {
    private QuickSession quickSession;
    private String s;

    private String template;
    private List<Object> values;
    private Map<String, Object> paramMap;
    private String outputString;

    @Parameterized.Parameters
    public static Collection<Object[]> data() {
        Object[][] objects = new Object[][]{
                {   // 0
                        "name",
                        "name",
                        Arrays.asList(),
                        createMap(),
                        "name",
                },
                {   // 1
                        "$name",
                        "?",
                        Arrays.asList("aaa"),
                        createMap(entry("name", "aaa")),
                        "aaa",
                },
                {   // 2
                        "name = $name",
                        "name = ?",
                        Arrays.asList("aaa"),
                        createMap(entry("name", "aaa")),
                        "name = aaa",
                },
                {   // 3
                        "age = $age",
                        "age = ?",
                        Arrays.asList(10),
                        createMap(entry("age", 10)),
                        "age = 10",
                },
                {   // 4
                        "name = $name and age = $age",
                        "name = ? and age = ?",
                        Arrays.asList("aaa", 10),
                        createMap(entry("name", "aaa"), entry("age", 10)),
                        "name = aaa and age = 10",
                },
                {   // 5
                        "name = $name and age = $age or id = $ids[1]",
                        "name = ? and age = ? or id = ?",
                        Arrays.asList("aaa", 10, 1),
                        createMap(entry("name", "aaa"), entry("age", 10), entry("ids[1]", 1)),
                        "name = aaa and age = 10 or id = 1",
                },
                {   // 6
                        "$user.username",
                        "?",
                        Arrays.asList("uuu"),
                        createMap(entry("user.username", "uuu")),
                        "uuu",
                },
        };
        return Arrays.asList(objects);
    }

    public TemplateExprTest(String s, String template, List<Object> values,
                            Map<String, Object> paramMap, String outputString) {
        this.quickSession = mockQuickSession();
        this.s = s;
        this.template = template;
        this.values = values;
        this.paramMap = paramMap;
        this.outputString = outputString;
    }

    @Test
    public void test() {
        TemplateExpr expr = new TemplateExpr(quickSession, s);
        assertEquals(template, expr.getTemplate());
        assertEquals(values, expr.getValues());
        assertEquals(paramMap, expr.getParamMap());
        assertEquals(outputString, expr.getString());
        assertEquals(outputString, TemplateExpr.getString(quickSession, s));
    }

    private static QuickSession mockQuickSession() {
        QuickSession quickSession = mock(QuickSession.class);
        when(quickSession.getParam("name")).thenReturn("aaa");
        when(quickSession.getParam("age")).thenReturn(10);
        when(quickSession.getParam("ids[1]")).thenReturn(1);
        when(quickSession.getParam("user.username")).thenReturn("uuu");
        return quickSession;
    }

    private static Map<String, Object> createMap(Bean... beans) {
        Map<String, Object> map = new HashMap<>();
        for (Bean bean : beans) {
            map.put(bean.key, bean.value);
        }
        return map;
    }

    private static Bean entry(String key, Object value) {
        return new Bean(key, value);
    }

    private static class Bean {
        private String key;
        private Object value;

        public Bean(String key, Object value) {
            this.key = key;
            this.value = value;
        }

        public String getKey() {
            return key;
        }

        public Object getValue() {
            return value;
        }
    }
}