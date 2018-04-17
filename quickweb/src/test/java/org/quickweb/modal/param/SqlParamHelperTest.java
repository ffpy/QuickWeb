package org.quickweb.modal.param;

import org.junit.Test;
import org.quickweb.exception.QuickWebException;
import org.quickweb.session.QuickSession;


import java.util.ArrayList;
import java.util.Arrays;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class SqlParamHelperTest {
    private QuickSession quickSession = mockQuickSession();

    private static QuickSession mockQuickSession() {
        QuickSession session = mock(QuickSession.class);
        when(session.getParam("tableName")).thenReturn("news");
        when(session.getParam("name")).thenReturn("xiaoming");
        when(session.getParam("age")).thenReturn(18);
        return session;
    }

    @Test
    public void getSqlParam() {
        SqlParam sqlParam = new SqlParam();
        SqlParamHelper helper = new SqlParamHelper(quickSession, sqlParam);
        assertEquals(sqlParam, helper.getSqlParam());
    }

    @Test
    public void getTable() {
        Object[][] objects = new Object[][]{
                {   // 0
                        null,
                        "",
                },
                {   // 1
                        new String[0],
                        "",
                },
                {   // 2
                        new String[]{"user"},
                        "user",
                },
                {   // 3
                        new String[]{"user", "info"},
                        "user,info",
                },
                {   // 4
                        new String[]{"user", "info", "order"},
                        "user,info,order",
                },
        };

        for (int i = 0; i < objects.length; i++) {
            SqlParam sqlParam = new SqlParam();
            sqlParam.setTables((String[]) objects[i][0]);
            SqlParamHelper helper = new SqlParamHelper(quickSession, sqlParam);
            assertEquals(String.valueOf(i), objects[i][1], helper.getTable());
        }
    }

    @Test
    public void getFirstTable() {
        Object[][] objects = new Object[][]{
                {   // 0
                        null,
                        "",
                },
                {   // 1
                        new String[0],
                        "",
                },
                {   // 2
                        new String[]{"user"},
                        "user",
                },
                {   // 3
                        new String[]{"user", "info"},
                        "user",
                },
                {   // 4
                        new String[]{"user", "info", "order"},
                        "user",
                },
                {   // 5
                        new String[]{"$user"},
                        "$user",
                },
        };

        for (int i = 0; i < objects.length; i++) {
            SqlParam sqlParam = new SqlParam();
            sqlParam.setTables((String[]) objects[i][0]);
            SqlParamHelper helper = new SqlParamHelper(quickSession, sqlParam);
            assertEquals(String.valueOf(i), objects[i][1], helper.getFirstTable());
        }
    }

    @Test
    public void getColumn() {
        Object[][] objects = new Object[][]{
                {   // 0
                        null,
                        "",
                },
                {   // 1
                        new String[0],
                        "",
                },
                {   // 2
                        new String[]{"username"},
                        "username",
                },
                {   // 3
                        new String[]{"username", "password"},
                        "username,password",
                },
                {   // 4
                        new String[]{"$name"},
                        "?",
                },
                {   // 5
                        new String[]{"$name", "age"},
                        "?,age",
                },
                {   // 6
                        new String[]{"$name", "$age"},
                        "?,?",
                },
                {   // 7
                        new String[]{"$unknown"},
                        "?",
                },
        };

        for (int i = 0; i < objects.length; i++) {
            SqlParam sqlParam = new SqlParam();
            sqlParam.setColumns((String[]) objects[i][0]);
            SqlParamHelper helper = new SqlParamHelper(quickSession, sqlParam);
            assertEquals(String.valueOf(i), objects[i][1], helper.getColumn());
        }
    }

    @Test
    public void getColumnCount() {
        Object[][] objects = new Object[][]{
                {   // 0
                        null,
                        0,
                },
                {   // 1
                        new String[0],
                        0,
                },
                {   // 2
                        new String[]{"username"},
                        1,
                },
                {   // 3
                        new String[]{"username", "password"},
                        2,
                },
                {   // 4
                        new String[]{"$name"},
                        1,
                },
        };

        for (int i = 0; i < objects.length; i++) {
            SqlParam sqlParam = new SqlParam();
            sqlParam.setColumns((String[]) objects[i][0]);
            SqlParamHelper helper = new SqlParamHelper(quickSession, sqlParam);
            assertEquals(String.valueOf(i), objects[i][1], helper.getColumnCount());
        }
    }

    @Test
    public void getColumnValues() {
        ArrayList<Object> list = new ArrayList<>();
        list.add(null);
        Object[][] objects = new Object[][]{
                {   // 0
                        null,
                        Arrays.asList(),
                },
                {   // 1
                        new String[0],
                        Arrays.asList(),
                },
                {   // 2
                        new String[]{"username"},
                        Arrays.asList(),
                },
                {   // 3
                        new String[]{"username", "password"},
                        Arrays.asList(),
                },
                {   // 4
                        new String[]{"$name"},
                        Arrays.asList("xiaoming"),
                },
                {   // 5
                        new String[]{"$name", "age"},
                        Arrays.asList("xiaoming"),
                },
                {   // 6
                        new String[]{"$name", "$age"},
                        Arrays.asList("xiaoming", 18),
                },
                {   // 7
                        new String[]{"$unknown"},
                        list,
                },
        };

        for (int i = 0; i < objects.length; i++) {
            SqlParam sqlParam = new SqlParam();
            sqlParam.setColumns((String[]) objects[i][0]);
            SqlParamHelper helper = new SqlParamHelper(quickSession, sqlParam);
            assertEquals(String.valueOf(i), objects[i][1], helper.getColumnValues());
        }
    }

    @Test
    public void getCondition() {
        Object[][] objects = new Object[][]{
                {   // 0
                        null,
                        "",
                },
                {   // 1
                        "",
                        "",
                },
                {   // 2
                        "age = 10",
                        "age = 10",
                },
                {   // 3
                        "age = $age",
                        "age = ?",
                },
                {   // 4
                        "name = $name and age = $age",
                        "name = ? and age = ?",
                },
        };

        for (int i = 0; i < objects.length; i++) {
            SqlParam sqlParam = new SqlParam();
            sqlParam.setCondition((String) objects[i][0]);
            SqlParamHelper helper = new SqlParamHelper(quickSession, sqlParam);
            assertEquals(String.valueOf(i), objects[i][1], helper.getCondition());
        }
    }

    @Test
    public void getConditionValues() {
        Object[][] objects = new Object[][]{
                {   // 0
                        null,
                        Arrays.asList(),
                },
                {   // 1
                        "",
                        Arrays.asList(),
                },
                {   // 2
                        "age = 10",
                        Arrays.asList(),
                },
                {   // 3
                        "age = $age",
                        Arrays.asList(18),
                },
                {   // 4
                        "name = $name and age = $age",
                        Arrays.asList("xiaoming", 18),
                },
                {   // 5
                        "name = $name and age = $age1",
                        Arrays.asList("xiaoming", null),
                },
        };

        for (int i = 0; i < objects.length; i++) {
            SqlParam sqlParam = new SqlParam();
            sqlParam.setCondition((String) objects[i][0]);
            SqlParamHelper helper = new SqlParamHelper(quickSession, sqlParam);
            assertEquals(String.valueOf(i), objects[i][1], helper.getConditionValues());
        }
    }

    @Test
    public void getOrder() {
        Object[][] objects = new Object[][]{
                {   // 0
                        null,
                        "",
                },
                {   // 1
                        new String[0],
                        "",
                },
                {   // 2
                        new String[]{"id"},
                        "id",
                },
                {   // 3
                        new String[]{"id desc", "name asc"},
                        "id desc,name asc",
                },
                {   // 4
                        new String[]{"$name desc"},
                        "? desc",
                },
                {   // 5
                        new String[]{"$name desc", "age asc"},
                        "? desc,age asc",
                },
                {   // 6
                        new String[]{"$name desc", "$age asc"},
                        "? desc,? asc",
                },
        };

        for (int i = 0; i < objects.length; i++) {
            SqlParam sqlParam = new SqlParam();
            sqlParam.setOrders((String[]) objects[i][0]);
            SqlParamHelper helper = new SqlParamHelper(quickSession, sqlParam);
            assertEquals(String.valueOf(i), objects[i][1], helper.getOrder());
        }
    }

    @Test
    public void getOrderValues() {
        Object[][] objects = new Object[][]{
                {   // 0
                        null,
                        Arrays.asList(),
                },
                {   // 1
                        new String[0],
                        Arrays.asList(),
                },
                {   // 2
                        new String[]{"id"},
                        Arrays.asList(),
                },
                {   // 3
                        new String[]{"id desc", "name asc"},
                        Arrays.asList(),
                },
                {   // 4
                        new String[]{"$name desc"},
                        Arrays.asList("xiaoming"),
                },
                {   // 5
                        new String[]{"$name desc", "age asc"},
                        Arrays.asList("xiaoming"),
                },
                {   // 6
                        new String[]{"$name desc", "$age asc"},
                        Arrays.asList("xiaoming", 18),
                },
                {   // 7
                        new String[]{"$name desc", "$age1 asc"},
                        Arrays.asList("xiaoming", null),
                },
        };

        for (int i = 0; i < objects.length; i++) {
            SqlParam sqlParam = new SqlParam();
            sqlParam.setOrders((String[]) objects[i][0]);
            SqlParamHelper helper = new SqlParamHelper(quickSession, sqlParam);
            assertEquals(String.valueOf(i), objects[i][1], helper.getOrderValues());
        }
    }

    @Test
    public void getOffset() {
        SqlParam sqlParam = new SqlParam();
        sqlParam.setOffset(0);
        SqlParamHelper helper = new SqlParamHelper(quickSession, sqlParam);
        assertEquals(0, helper.getOffset());

        sqlParam = new SqlParam();
        sqlParam.setOffset(1);
        helper = new SqlParamHelper(quickSession, sqlParam);
        assertEquals(1, helper.getOffset());
    }

    @Test(expected = QuickWebException.class)
    public void getOffsetException() {
        SqlParam sqlParam = new SqlParam();
        sqlParam.setOffset(-1);
        SqlParamHelper helper = new SqlParamHelper(quickSession, sqlParam);
        helper.getOffset();
    }

    @Test
    public void getLimit() {
        SqlParam sqlParam = new SqlParam();
        sqlParam.setLimit(0);
        SqlParamHelper helper = new SqlParamHelper(quickSession, sqlParam);
        assertEquals(0, helper.getLimit());

        sqlParam = new SqlParam();
        sqlParam.setLimit(1);
        helper = new SqlParamHelper(quickSession, sqlParam);
        assertEquals(1, helper.getLimit());
    }

    @Test(expected = QuickWebException.class)
    public void getLimitException() {
        SqlParam sqlParam = new SqlParam();
        sqlParam.setLimit(-1);
        SqlParamHelper helper = new SqlParamHelper(quickSession, sqlParam);
        helper.getLimit();
    }
}