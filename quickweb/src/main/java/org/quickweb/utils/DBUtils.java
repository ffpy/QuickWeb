package org.quickweb.utils;

import com.sun.istack.internal.Nullable;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DBUtils {
    static {
        try {
            Class.forName("com.mysql.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static Connection getConnection(String url, @Nullable String user, @Nullable String pwd) {
        ObjectUtils.requireNonNull(url);

        try {
            return DriverManager.getConnection(url, user, pwd);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void close(Connection connection) {
        close(connection, null);
    }

    public static void close(Statement statement) {
        close(null, statement);
    }

    public static void close(Connection connection, Statement statement) {
        try {
            if (connection != null)
                connection.close();
            if (statement != null)
                statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
