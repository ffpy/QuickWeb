package org.quickweb.modal.param;

import org.quickweb.session.QuickSession;
import org.quickweb.template.TemplateExpr;
import org.quickweb.utils.EmptyUtils;
import org.quickweb.utils.StringUtils;

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
        String s = StringUtils.join(sqlParam.getTables(), SEPARATOR);
        if (s == null)
            s = "";
        return s;
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
        return columnExpr.getPlaceholderString(PLACEHOLDER);
    }

    public int getColumnCount() {
        if (sqlParam.getColumns() == null)
            return 0;
        return sqlParam.getColumns().length;
    }

    public List<Object> getColumnValues() {
        if (EmptyUtils.isEmpty(sqlParam.getColumns()))
            return Collections.emptyList();

        if (columnExpr == null)
            getColumn();

        return columnExpr.getValues();
    }

    public String getCondition() {
        if (StringUtils.isEmpty(sqlParam.getCondition()))
            return "";

        if (conditionExpr == null)
            conditionExpr = new TemplateExpr(quickSession, sqlParam.getCondition());
        return conditionExpr.getPlaceholderString(PLACEHOLDER);
    }

    public List<Object> getConditionValues() {
        if (StringUtils.isEmpty(sqlParam.getCondition()))
            return Collections.emptyList();

        if (conditionExpr == null)
            getCondition();

        return conditionExpr.getValues();
    }

    public String getOrder() {
        if (EmptyUtils.isEmpty(sqlParam.getOrders()))
            return "";

        String order = StringUtils.join(sqlParam.getOrders(), SEPARATOR);
        if (orderExpr == null)
            orderExpr = new TemplateExpr(quickSession, order);
        return orderExpr.getPlaceholderString(PLACEHOLDER);
    }

    public List<Object> getOrderValues() {
        if (EmptyUtils.isEmpty(sqlParam.getOrders()))
            return Collections.emptyList();

        if (orderExpr == null)
            getOrder();

        return orderExpr.getValues();
    }

    public int getOffset() {
        return sqlParam.getOffset();
    }

    public int getLimit() {
        return sqlParam.getLimit();
    }
}
