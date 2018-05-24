package org.quickweb.modal.bulider;

import com.sun.istack.internal.Nullable;
import org.quickweb.modal.param.SqlParamHelper;
import org.quickweb.utils.ExceptionUtils;
import org.quickweb.utils.StringUtils;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class MysqlBuilder implements SqlBuilder {

    @Override
    public String insert(SqlParamHelper helper) {
        return "INSERT INTO " + helper.getFirstTable() +
                "(" + helper.getColumn() + ")" +
                " VALUES(" + buildPlaceholders(helper.getColumnCount()) + ")";
    }

    @Override
    public String update(SqlParamHelper helper) {
        List<String> sets = Arrays.stream(helper.getSqlParam().getColumns())
                .map(param -> param + " = ?")
                .collect(Collectors.toList());

        String sql = "UPDATE " + helper.getFirstTable() +
                " SET " + StringUtils.join(sets, ',');
        sql += concat(" WHERE ", helper.getCondition());
        return sql;
    }

    @Override
    public String delete(SqlParamHelper helper) {
        String sql = "DELETE FROM " + helper.getFirstTable();
        sql += concat(" WHERE ", helper.getCondition());
        return sql;
    }

    @Override
    public String find(SqlParamHelper helper) {
        String sql = "SELECT " + helper.getColumn() + " FROM " + helper.getTable();
        sql += concat(" WHERE ", helper.getCondition());
        sql += concat(" ORDER BY ", helper.getOrder());
        if (helper.getLimit() >= 0) {
            sql += " LIMIT " + helper.getLimit();
            if (helper.getOffset() >= 0)
                sql += " OFFSET " + helper.getOffset();
        }
        return sql;
    }

    protected String concat(String prefix, @Nullable String content) {
        if (StringUtils.isEmpty(content))
            return "";
        return prefix + content;
    }

    /**
     * 生成n个占位符的字符串
     */
    protected String buildPlaceholders(int n) {
        if (n <= 0)
            throw ExceptionUtils.mustMoreThan("n", 0);

        char[] placeholders = new char[n];
        for (int i = 0; i < n; i++) {
            placeholders[i] = '?';
        }
        return StringUtils.join(placeholders, ',');
    }
}