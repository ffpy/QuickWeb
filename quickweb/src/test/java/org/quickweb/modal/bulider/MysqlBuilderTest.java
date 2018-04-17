package org.quickweb.modal.bulider;

import org.junit.Test;
import org.quickweb.exception.QuickWebException;
import org.quickweb.modal.param.SqlParam;
import org.quickweb.modal.param.SqlParamHelper;
import org.quickweb.session.QuickSession;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class MysqlBuilderTest {
    private MysqlBuilder builder = new MysqlBuilder();
    private QuickSession quickSession = createQuickSession();

    private QuickSession createQuickSession() {
        QuickSession quickSession = mock(QuickSession.class);
        when(quickSession.getParam("username")).thenReturn("aaa");
        when(quickSession.getParam("password")).thenReturn("111");
        when(quickSession.getParam("name")).thenReturn("xiaoming");
        when(quickSession.getParam("age")).thenReturn("18");
        return quickSession;
    }

    @Test
    public void insert() {
        Object[][] objects = new Object[][]{
                {   // 0
                        new String[]{"user"},
                        new String[]{"username", "password"},
                        "INSERT INTO user(username,password) VALUES(?,?)",
                },
                {   // 1
                        new String[]{"user"},
                        new String[]{"name"},
                        "INSERT INTO user(name) VALUES(?)",
                },
                {   // 2
                        new String[]{"user", "info"},
                        new String[]{"name"},
                        "INSERT INTO user(name) VALUES(?)",
                },
        };

        for (int i = 0; i < objects.length; i++) {
            SqlParam sqlParam = new SqlParam();
            sqlParam.setTables((String[]) objects[i][0]);
            sqlParam.setColumns((String[]) objects[i][1]);

            SqlParamHelper helper = new SqlParamHelper(quickSession, sqlParam);
            String result = builder.insert(helper);
            assertEquals(String.valueOf(i), objects[i][2], result);
        }
    }

    @Test
    public void update() {
        Object[][] objects = new Object[][]{
                {   // 0
                        new String[]{"user"},
                        new String[]{"name"},
                        null,
                        "UPDATE user SET name = ?",
                },
                {   // 1
                        new String[]{"user"},
                        new String[]{"name", "age"},
                        null,
                        "UPDATE user SET name = ?,age = ?",
                },
                {   // 2
                        new String[]{"user"},
                        new String[]{"name"},
                        "age = 10",
                        "UPDATE user SET name = ? WHERE age = 10",
                },
                {   // 3
                        new String[]{"user"},
                        new String[]{"name", "age"},
                        "age = 10",
                        "UPDATE user SET name = ?,age = ? WHERE age = 10",
                },
                {   // 4
                        new String[]{"user", "info"},
                        new String[]{"name", "age"},
                        null,
                        "UPDATE user SET name = ?,age = ?",
                },
                {   // 5
                        new String[]{"user", "info"},
                        new String[]{"name", "age"},
                        "",
                        "UPDATE user SET name = ?,age = ?",
                },
        };

        for (int i = 0; i < objects.length; i++) {
            SqlParam sqlParam = new SqlParam();
            sqlParam.setTables((String[]) objects[i][0]);
            sqlParam.setColumns((String[]) objects[i][1]);
            sqlParam.setCondition((String) objects[i][2]);

            SqlParamHelper helper = new SqlParamHelper(quickSession, sqlParam);
            String result = builder.update(helper);
            assertEquals(String.valueOf(i), objects[i][3], result);
        }
    }

    @Test
    public void delete() {
        Object[][] objects = new Object[][]{
                {   // 0
                        new String[]{"user"},
                        null,
                        "DELETE FROM user",
                },
                {   // 1
                        new String[]{"user"},
                        "",
                        "DELETE FROM user",
                },
                {   // 2
                        new String[]{"user"},
                        "age >= 18",
                        "DELETE FROM user WHERE age >= 18",
                },
        };

        for (int i = 0; i < objects.length; i++) {
            SqlParam sqlParam = new SqlParam();
            sqlParam.setTables((String[]) objects[i][0]);
            sqlParam.setCondition((String) objects[i][1]);

            SqlParamHelper helper = new SqlParamHelper(quickSession, sqlParam);
            String result = builder.delete(helper);
            assertEquals(String.valueOf(i), objects[i][2], result);
        }
    }

    @Test
    public void find() {
        Object[][] objects = new Object[][]{
                {   // 0
                        new String[]{"*"},
                        new String[]{"user"},
                        null, null, null, null,
                        "SELECT * FROM user",
                },
                {   // 1
                        new String[]{"*"},
                        new String[]{"user", "info"},
                        null, null, null, null,
                        "SELECT * FROM user,info",
                },
                {   // 2
                        new String[]{"username", "password"},
                        new String[]{"user"},
                        null, null, null, null,
                        "SELECT username,password FROM user",
                },
                {   // 3
                        new String[]{"user.username", "info.idcard"},
                        new String[]{"user", "info"},
                        null, null, null, null,
                        "SELECT user.username,info.idcard FROM user,info",
                },
                {   // 4
                        new String[]{"*"},
                        new String[]{"user"},
                        "age > 10", null, null, null,
                        "SELECT * FROM user WHERE age > 10",
                },
                {   // 5
                        new String[]{"*"},
                        new String[]{"user"},
                        null, new String[]{"id desc"}, null, null,
                        "SELECT * FROM user ORDER BY id desc",
                },
                {   // 6
                        new String[]{"*"},
                        new String[]{"user"},
                        null, new String[]{"id desc", "name asc"}, null, null,
                        "SELECT * FROM user ORDER BY id desc,name asc",
                },
                {   // 7
                        new String[]{"*"},
                        new String[]{"user"},
                        "age > 18", new String[]{"id desc", "name asc"}, null, null,
                        "SELECT * FROM user WHERE age > 18 ORDER BY id desc,name asc",
                },
                {   // 8
                        new String[]{"*"},
                        new String[]{"user"},
                        null, null, 10, null,
                        "SELECT * FROM user",
                },
                {   // 9
                        new String[]{"*"},
                        new String[]{"user"},
                        null, null, null, 5,
                        "SELECT * FROM user LIMIT 5",
                },
                {   // 10
                        new String[]{"*"},
                        new String[]{"user"},
                        null, null, 10, 5,
                        "SELECT * FROM user LIMIT 5 OFFSET 10",
                },
                {   // 11
                        new String[]{"*"},
                        new String[]{"user"},
                        "age > 18", new String[]{"id desc", "name asc"}, 10, 20,
                        "SELECT * FROM user WHERE age > 18 ORDER BY id desc,name asc LIMIT 20 OFFSET 10",
                },
        };

        for (int i = 0; i < objects.length; i++) {
            SqlParam sqlParam = new SqlParam();
            sqlParam.setColumns((String[]) objects[i][0]);
            sqlParam.setTables((String[]) objects[i][1]);
            sqlParam.setCondition((String) objects[i][2]);
            sqlParam.setOrders((String[]) objects[i][3]);
            if (objects[i][4] != null)
                sqlParam.setOffset((Integer) objects[i][4]);
            if (objects[i][5] != null)
                sqlParam.setLimit((Integer) objects[i][5]);

            SqlParamHelper helper = new SqlParamHelper(quickSession, sqlParam);
            String result = builder.find(helper);
            assertEquals(String.valueOf(i), objects[i][6], result);
        }
    }

    @Test
    public void concat() {
        String[][] strings = new String[][]{
                {   // 0
                        " where age = 10",
                        builder.concat(" where ", "age = 10"),
                },
                {   // 1
                        "",
                        builder.concat(" where ", ""),
                },
                {   // 2
                        "",
                        builder.concat(" where ", null),
                },
        };
        for (int i = 0; i < strings.length; i++) {
            assertEquals(String.valueOf(i), strings[i][0], strings[i][1]);
        }
    }

    @Test(expected = QuickWebException.class)
    public void buildPlaceholdersException() {
        builder.buildPlaceholders(0);
    }

    @Test(expected = QuickWebException.class)
    public void buildPlaceholdersException1() {
        builder.buildPlaceholders(-1);
    }

    @Test
    public void buildPlaceholders() {
        assertEquals("?", builder.buildPlaceholders(1));
        assertEquals("?,?", builder.buildPlaceholders(2));
        assertEquals("?,?,?", builder.buildPlaceholders(3));
    }
}