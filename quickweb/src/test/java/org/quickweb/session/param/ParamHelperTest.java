package org.quickweb.session.param;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.quickweb.exception.QuickWebException;
import org.quickweb.session.scope.Scope;

import java.util.Arrays;
import java.util.Collection;

import static org.junit.Assert.*;

@RunWith(Parameterized.class)
public class ParamHelperTest {
    private String param;
    private String scopeName;
    private String paramName;
    private String members;
    private ParamMember[] memberArr;
    private Scope scope;
    private Class<? extends Exception> exceptionType;

    @Parameterized.Parameters
    public static Collection<Object[]> data() {
        Object[][] objects = new Object[][]{
                {   // 0
                        "",
                        "", "", "",
                        new ParamMember[0],
                        Scope.ALL, QuickWebException.class,
                },
                {   // 1
                        ":",
                        "", "", "",
                        new ParamMember[0],
                        Scope.ALL, QuickWebException.class,
                },
                {   // 2
                        "name",
                        "", "name", "",
                        new ParamMember[0],
                        Scope.ALL, null,
                },
                {   // 3
                        "x:name",
                        "x", "name", "",
                        new ParamMember[0],
                        Scope.CONTEXT, null,
                },
                {   // 4
                        "context:name",
                        "context", "name", "",
                        new ParamMember[0],
                        Scope.CONTEXT, null,
                },
                {   // 5
                        "m:name",
                        "m", "name", "",
                        new ParamMember[0],
                        Scope.MODAL, null,
                },
                {   // 6
                        "modal:name",
                        "modal", "name", "",
                        new ParamMember[0],
                        Scope.MODAL, null,
                },
                {   // 7
                        "r:name",
                        "r", "name", "",
                        new ParamMember[0],
                        Scope.REQUEST, null,
                },
                {   // 8
                        "request:name",
                        "request", "name", "",
                        new ParamMember[0],
                        Scope.REQUEST, null,
                },
                {   // 9
                        "s:name",
                        "s", "name", "",
                        new ParamMember[0],
                        Scope.SESSION, null,
                },
                {   // 10
                        "session:name",
                        "session", "name", "",
                        new ParamMember[0],
                        Scope.SESSION, null,
                },
                {   // 11
                        "c:name",
                        "c", "name", "",
                        new ParamMember[0],
                        Scope.COOKIE, null,
                },
                {   // 12
                        "cookie:name",
                        "cookie", "name", "",
                        new ParamMember[0],
                        Scope.COOKIE, null,
                },
                {   // 13
                        "a:name",
                        "a", "name", "",
                        new ParamMember[0],
                        Scope.APPLICATION, null,
                },
                {   // 14
                        "application:name",
                        "application", "name", "",
                        new ParamMember[0],
                        Scope.APPLICATION, null,
                },
                {   // 15
                        ":name",
                        "", "name", "",
                        new ParamMember[0],
                        Scope.ALL, null,
                },
                {   // 16
                        "all:name",
                        "all", "name", "",
                        new ParamMember[0],
                        Scope.ALL, null,
                },
                {   // 17
                        "r:name.a",
                        "r", "name", "a",
                        new ParamMember[]{
                                new ParamMember("a", false)
                        },
                        Scope.REQUEST, null,
                },
                {   // 18
                        "r:name.a.b",
                        "r", "name", "a.b",
                        new ParamMember[]{
                                new ParamMember("a", false),
                                new ParamMember("b", false),
                        },
                        Scope.REQUEST, null,
                },
                {   // 19
                        "r:name[0]",
                        "r", "name", "[0]",
                        new ParamMember[]{
                                new ParamMember("0", true),
                        },
                        Scope.REQUEST, null,
                },
                {   // 20
                        "r:name[0].a",
                        "r", "name", "[0].a",
                        new ParamMember[]{
                                new ParamMember("0", true),
                                new ParamMember("a", false),
                        },
                        Scope.REQUEST, null,
                },
                {   // 21
                        "r:name[0][1].a[2]",
                        "r", "name", "[0][1].a[2]",
                        new ParamMember[]{
                                new ParamMember("0", true),
                                new ParamMember("1", true),
                                new ParamMember("a", false),
                                new ParamMember("2", true),
                        },
                        Scope.REQUEST, null,
                },
        };
        return Arrays.asList(objects);
    }

    public ParamHelperTest(String param, String scopeName, String paramName, String members,
                           ParamMember[] memberArr, Scope scope, Class<? extends Exception> exceptionType) {
        this.param = param;
        this.scopeName = scopeName;
        this.paramName = paramName;
        this.members = members;
        this.memberArr = memberArr;
        this.scope = scope;
        this.exceptionType = exceptionType;
    }

    @Test
    public void test() {
        Exception ex = null;
        try {
            ParamHelper helper = new ParamHelper(param);
            assertEquals(scopeName, helper.getScopeName());
            assertEquals(paramName, helper.getParamName());
            assertEquals(members, helper.getMembers());
            assertEquals(scope, helper.getScope());
            for (ParamMember aMemberArr : memberArr) {
                assertEquals(true, helper.hasMember());
                assertEquals(aMemberArr, helper.findMember());
            }
            assertEquals(false, helper.hasMember());
        } catch (Exception e) {
            ex = e;
        }
        if (ex == null)
            assertEquals(exceptionType, null);
        else
            assertEquals(ex.getMessage(), exceptionType, ex.getClass());
    }
}