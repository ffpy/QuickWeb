package org.quickweb.modal.handler;

import org.apache.commons.lang3.StringUtils;
import org.quickweb.modal.QuickModal;
import org.quickweb.modal.ResultType;
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
        sqlParam.setSelect("COUNT(*) AS count");

        findAction(quickModal, paramName, sqlParam, (stmt, rs) -> {
            int count = 0;
            if (rs.next())
                count = rs.getInt("count");
            quickModal.getQuickSession().putParam(paramName, count, EditableParamScope.MODAL);
        });
    }

    public static void avg(QuickModal quickModal, String paramName, String column, SqlParam sqlParam) {

    }

    public static void max(QuickModal quickModal, String paramName, String column, ResultType resultType, SqlParam sqlParam) {

    }

    public static void min(QuickModal quickModal, String paramName, String column, ResultType resultType, SqlParam sqlParam) {

    }

    public static void sum(QuickModal quickModal, String paramName, String column, ResultType resultType, SqlParam sqlParam) {

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
}
