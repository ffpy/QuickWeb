package org.quickweb.template;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.Collection;

import static org.junit.Assert.*;

@RunWith(Parameterized.class)
public class MatcherHelperTest {
    private String template;
    private String[] matchs;
    private String[] params;
    private String[] scopeNames;

    @Parameterized.Parameters
    public static Collection<Object[]> data() {
        Object[][] objects = new Object[][]{
                {   // 0
                        "",
                        new String[0],
                        new String[0],
                        new String[0],
                },
                {   // 1
                        "name",
                        new String[0],
                        new String[0],
                        new String[0],
                },
                {   // 2
                        "name = $",
                        new String[]{"$"},
                        new String[]{""},
                        new String[]{""},
                },
                {   // 3
                        "name = $name",
                        new String[]{"$name"},
                        new String[]{"name"},
                        new String[]{""},
                },
                {   // 4
                        "name = $:",
                        new String[]{"$:"},
                        new String[]{":"},
                        new String[]{""},
                },
                {   // 5
                        "name = $:name",
                        new String[]{"$:name"},
                        new String[]{":name"},
                        new String[]{""},
                },
                {   // 6
                        "name = $r:",
                        new String[]{"$r:"},
                        new String[]{"r:"},
                        new String[]{"r"},
                },
                {   // 7
                        "name = $r:name",
                        new String[]{"$r:name"},
                        new String[]{"r:name"},
                        new String[]{"r"}
                },
                {   // 8
                        "$newName = $name",
                        new String[]{"$newName", "$name"},
                        new String[]{"newName", "name"},
                        new String[]{"", ""},
                },
                {   // 9
                        "$r:newName = $s:name",
                        new String[]{"$r:newName", "$s:name"},
                        new String[]{"r:newName", "s:name"},
                        new String[]{"r", "s"},
                },
                {   // 10
                        "$name.a",
                        new String[]{"$name.a"},
                        new String[]{"name.a"},
                        new String[]{""},
                },
                {   // 11
                        "$name[0]",
                        new String[]{"$name[0]"},
                        new String[]{"name[0]"},
                        new String[]{""},
                },
                {   // 12
                        "$name[0].a[1]",
                        new String[]{"$name[0].a[1]"},
                        new String[]{"name[0].a[1]"},
                        new String[]{""},
                },
        };
        return Arrays.asList(objects);
    }

    public MatcherHelperTest(String template, String[] matchs, String[] params, String[] scopeNames) {
        this.template = template;
        this.matchs = matchs;
        this.params = params;
        this.scopeNames = scopeNames;
    }

    @Test
    public void test() {
        MatcherHelper helper = new MatcherHelper(template);
        for (int i = 0; i < matchs.length; i++) {
            assertEquals(helper.find(), true);
            assertEquals(matchs[i], helper.getMatch());
            assertEquals(params[i], helper.getParam());
            assertEquals(scopeNames[i], helper.getScopeName());
        }
        assertEquals(helper.find(), false);
    }
}