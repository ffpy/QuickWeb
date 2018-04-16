package org.quickweb.modal.bulider;

import com.sun.istack.internal.Nullable;
import org.apache.commons.lang3.StringUtils;
import org.quickweb.modal.param.SqlParamHelper;
import org.quickweb.utils.ExceptionUtils;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public abstract class SqlBuilder {

    public String insert(SqlParamHelper helper) {
        return "INSERT INTO " + helper.getTable() +
                "(" + helper.getColumn() + ")" +
                " VALUES(" + buildPlaceholders(helper.getSqlParam().getColumns().length) + ")";
    }

    public String update(SqlParamHelper helper) {
        List<String> sets = Arrays.stream(helper.getSqlParam().getColumns())
                .map(param -> param + " = ?")
                .collect(Collectors.toList());

        String sql = "UPDATE " + helper.getTable() +
                " SET " + StringUtils.join(sets, ',');
        sql += buildWhere(helper.getCondition());
        return sql;
    }

    public String delete(SqlParamHelper helper) {
        String sql = "DELETE FROM " + helper.getTable();
        sql += buildWhere(helper.getCondition());
        return sql;
    }

    public abstract String find(SqlParamHelper helper);

    /**
     * 生成where子句
     * @param condition
     * @return
     */
    protected String buildWhere(@Nullable String condition) {
        if (StringUtils.isEmpty(condition))
            return "";
        return " WHERE " + condition;
    }

    /**
     * 生成n个占位符的字符串
     * @param n
     * @return
     */
    protected String buildPlaceholders(int n) {
        if (n <= 0)
            ExceptionUtils.throwMustMoreThanException("n", 0);

        char[] placeholders = new char[n];
        for (int i = 0; i < n; i++) {
            placeholders[i] = '?';
        }
        return StringUtils.join(placeholders, ',');
    }
}
