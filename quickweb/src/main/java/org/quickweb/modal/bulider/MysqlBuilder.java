package org.quickweb.modal.bulider;

import org.apache.commons.lang3.StringUtils;
import org.quickweb.modal.param.SqlParamHelper;

public class MysqlBuilder extends SqlBuilder {

    @Override
    public String find(SqlParamHelper helper) {
        String sql = "SELECT " + helper.getColumn() + " FROM " + helper.getTable();
        sql += buildWhere(helper.getCondition());
        if (!StringUtils.isEmpty(helper.getOrder()))
            sql += " ORDER BY " + helper.getOrder();
        if (helper.getLimit() >= 0) {
            sql += " LIMIT " + helper.getLimit();
            if (helper.getOffset() >= 0)
                sql += " OFFSET " + helper.getOffset();
        }
        return sql;
    }
}