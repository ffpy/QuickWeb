package org.quickweb.utils;

import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class SqlUtils {

    public static String insert(String table, String[] params) {
        ObjectUtils.requireNotNull(table, params);
        ObjectUtils.requireCollectionNotEmpty(params);

        return "INSERT INTO " + table +
                "(" + StringUtils.join(params, ',') + ")" +
                " VALUES(" + buildPlaceholders(params.length) + ")";
    }

    public static String update(String table, String[] params, String where) {
        ObjectUtils.requireNotNull(table, params);
        ObjectUtils.requireCollectionNotEmpty(params);

        List<String> sets = Arrays.stream(params)
                .map(param -> param + " = ?")
                .collect(Collectors.toList());

        return "UPDATE " + table +
                " SET " + StringUtils.join(sets, ',') +
                " WHERE " + where;
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
