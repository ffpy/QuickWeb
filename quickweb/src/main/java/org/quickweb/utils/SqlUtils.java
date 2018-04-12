package org.quickweb.utils;

import com.sun.istack.internal.Nullable;
import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class SqlUtils {

    public static String insert(String table, String[] columns) {
        RequireUtils.requireNotEmpty(table);
        RequireUtils.requireCollectionNotEmpty(columns);

        return "INSERT INTO " + table +
                "(" + StringUtils.join(columns, ',') + ")" +
                " VALUES(" + buildPlaceholders(columns.length) + ")";
    }

    public static String update(String table, String[] columns, @Nullable String where) {
        RequireUtils.requireNotEmpty(table);
        RequireUtils.requireCollectionNotEmpty(columns);

        List<String> sets = Arrays.stream(columns)
                .map(param -> param + " = ?")
                .collect(Collectors.toList());

        String sql = "UPDATE " + table +
                " SET " + StringUtils.join(sets, ',');
        sql += buildWhere(where);
        return sql;
    }

    public static String delete(String table, @Nullable String where) {
        RequireUtils.requireNotEmpty(table);

        String sql = "DELETE FROM " + table;
        sql += buildWhere(where);
        return sql;
    }

    public static String find(String table, String select, @Nullable String where,
                              @Nullable String order, int offset, int limit) {
        RequireUtils.requireNotEmpty(table, select);

        String sql = "SELECT " + select +
                " FROM " + table;
        sql += buildWhere(where);
        if (!StringUtils.isEmpty(order))
            sql += " ORDER BY " + order;
        if (limit >= 0) {
            sql += " LIMIT " + limit;
            if (offset >= 0)
                sql += " OFFSET " + offset;
        }
        return sql;
    }

    private static String buildWhere(@Nullable String where) {
        if (StringUtils.isEmpty(where))
            return "";
        return " WHERE " + where;
    }

    private static String buildPlaceholders(int n) {
        if (n <= 0)
            throw new RuntimeException("n must be more than zero");

        char[] placeholders = new char[n];
        for (int i = 0; i < n; i++) {
            placeholders[i] = '?';
        }
        return StringUtils.join(placeholders, ',');
    }
}
