package org.quickweb.modal.param;

import org.apache.commons.lang3.StringUtils;
import org.quickweb.session.QuickSession;
import org.quickweb.template.TemplateExpr;
import org.quickweb.utils.EmptyUtils;

import java.util.Collections;
import java.util.List;

public class SqlParamHelper {
    private static final char SEPARATOR = ',';
    private static final String PLACEHOLDER = "?";

    private QuickSession quickSession;
    private SqlParam sqlParam;
    private TemplateExpr columnExpr;
    private TemplateExpr conditionExpr;
    private TemplateExpr orderExpr;

    public SqlParamHelper(QuickSession quickSession, SqlParam sqlParam) {
        this.quickSession = quickSession;
        this.sqlParam = sqlParam;
    }

    public SqlParam getSqlParam() {
        return sqlParam;
    }

    public String getTable() {
        return StringUtils.join(sqlParam.getTables(), SEPARATOR);
    }

    public String getFirstTable() {
        if (EmptyUtils.isEmpty(sqlParam.getTables()))
            return "";
        return sqlParam.getTables()[0];
    }

    public String getColumn() {
        if (EmptyUtils.isEmpty(sqlParam.getColumns()))
            return "";

        String column = StringUtils.join(sqlParam.getColumns(), SEPARATOR);
        if (columnExpr == null)
            columnExpr = new TemplateExpr(quickSession, column);
        return columnExpr.getTemplate(PLACEHOLDER);
    }

    public int getColumnCount() {
        if (sqlParam.getColumns() == null)
            return 0;
        return sqlParam.getColumns().length;
    }

    public List<Object> getColumnValues() {
        if (EmptyUtils.isEmpty(sqlParam.getColumns()))
            return Collections.emptyList();

        return columnExpr.getValues();
    }

    public String getCondition() {
        if (StringUtils.isEmpty(sqlParam.getCondition()))
            return "";

        if (conditionExpr == null)
            conditionExpr = new TemplateExpr(quickSession, sqlParam.getCondition());
        return conditionExpr.getTemplate(PLACEHOLDER);
    }

    public List<Object> getConditionValues() {
        if (StringUtils.isEmpty(sqlParam.getCondition()))
            return Collections.emptyList();

        return conditionExpr.getValues();
    }

    public String getOrder() {
        if (EmptyUtils.isEmpty(sqlParam.getOrders()))
            return "";

        String order = StringUtils.join(sqlParam.getOrders(), SEPARATOR);
        if (orderExpr == null)
            orderExpr = new TemplateExpr(quickSession, order);
        return orderExpr.getTemplate(PLACEHOLDER);
    }

    public List<Object> getOrderValues() {
        if (EmptyUtils.isEmpty(sqlParam.getOrders()))
            return Collections.emptyList();

        return orderExpr.getValues();
    }

    public int getOffset() {
        return sqlParam.getOffset();
    }

    public int getLimit() {
        return sqlParam.getLimit();
    }
}
