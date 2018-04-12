package org.quickweb.modal.handler;

import com.sun.istack.internal.Nullable;
import org.apache.commons.lang3.StringUtils;
import org.quickweb.modal.QuickModal;
import org.quickweb.modal.SqlParam;
import org.quickweb.modal.StmtHelper;
import org.quickweb.session.EditableParamScope;
import org.quickweb.template.TemplateExpr;
import org.quickweb.utils.RequireUtils;
import org.quickweb.utils.SqlUtils;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class QueryHandler {

    public static void find(QuickModal quickModal, String paramName, SqlParam sqlParam) {
        findAction(quickModal, paramName, sqlParam, (stmt, rs) -> {
            List<Map<String, Object>> rowList = new ArrayList<>();
            ResultSetMetaData metaData = rs.getMetaData();

            while (rs.next()) {
                Map<String, Object> rowMap = new HashMap<>();
                for (int i = 1; i <= metaData.getColumnCount(); i++) {
                    Object obj = rs.getObject(i);
                    rowMap.put(metaData.getColumnLabel(i), obj);
                }
                rowList.add(rowMap);
            }
            quickModal.getQuickSession().putParam(paramName, rowList, EditableParamScope.MODAL);
        });
    }

    public static void findFirst(QuickModal quickModal, String paramName, SqlParam sqlParam) {
        findAction(quickModal, paramName, sqlParam, (stmt, rs) -> {
            ResultSetMetaData metaData = rs.getMetaData();
            Map<String, Object> rowMap = new HashMap<>();
            if (rs.next()) {
                for (int i = 1; i <= metaData.getColumnCount(); i++) {
                    Object obj = rs.getObject(i);
                    rowMap.put(metaData.getColumnLabel(i), obj);
                }
            }
            quickModal.getQuickSession().putParam(paramName, rowMap, EditableParamScope.MODAL);
        });
    }

    public static void count(QuickModal quickModal, String paramName, SqlParam sqlParam) {
        aggregateAction(quickModal, paramName, "*", sqlParam, "COUNT", Integer.class);
    }

    public static void avg(QuickModal quickModal, String paramName, String column, SqlParam sqlParam) {
        aggregateAction(quickModal, paramName, column, sqlParam, "AVG", Double.class);
    }

    public static void max(QuickModal quickModal, String paramName, String column, SqlParam sqlParam) {
        aggregateAction(quickModal, paramName, column, sqlParam, "MAX");
    }

    public static void min(QuickModal quickModal, String paramName, String column, SqlParam sqlParam) {
        aggregateAction(quickModal, paramName, column, sqlParam, "MIN");
    }

    public static void sum(QuickModal quickModal, String paramName, String column, SqlParam sqlParam) {
        aggregateAction(quickModal, paramName, column, sqlParam, "SUM");
    }

    private static void findAction(QuickModal quickModal, String paramName, SqlParam sqlParam,
                                   FindAction action) {
        RequireUtils.requireNotEmpty(paramName);
        RequireUtils.requireNotNull(sqlParam, action);

        String select = sqlParam.getSelect();
        if (StringUtils.isEmpty(select))
            select = "*";

        TemplateExpr selectExpr = new TemplateExpr(quickModal.getQuickSession(), select);
        TemplateExpr whereExpr = new TemplateExpr(quickModal.getQuickSession(), sqlParam.getWhere());
        TemplateExpr orderExpr = new TemplateExpr(quickModal.getQuickSession(), sqlParam.getOrder());
        String sql = SqlUtils.find(sqlParam.getTable(), selectExpr.getTemplate(), whereExpr.getTemplate(),
                orderExpr.getTemplate(), sqlParam.getOffset(), sqlParam.getLimit());

        DataHandler.handle(quickModal, sql, (conn, stmt) -> {
            StmtHelper stmtHelper = new StmtHelper(quickModal.getQuickSession(), stmt);
            stmtHelper.setParams(selectExpr.getValues());
            stmtHelper.setParams(whereExpr.getValues());
            stmtHelper.setParams(orderExpr.getValues());

            ResultSet rs = stmt.executeQuery();
            action.act(stmt, rs);
        });
    }

    private static void aggregateAction(QuickModal quickModal, String paramName, String column,
                                        SqlParam sqlParam, String method) {
        aggregateAction(quickModal, paramName, column, sqlParam, method, null);
    }

    private static void aggregateAction(QuickModal quickModal, String paramName, String column,
                                        SqlParam sqlParam, String method, @Nullable Class<?> resultType) {
        RequireUtils.requireNotEmpty(method, column);
        sqlParam.setSelect(method + "(" + column + ")");

        findAction(quickModal, paramName, sqlParam, (stmt, rs) -> {
            if (rs.next()) {
                Object value;
                if (resultType == null)
                    value = rs.getObject(1);
                else
                    value = rs.getObject(1, resultType);

                quickModal.getQuickSession().putParam(paramName, value, EditableParamScope.MODAL);
            }
        });
    }
}
