package org.quickweb.modal;

import org.apache.commons.lang3.StringUtils;
import org.quickweb.QuickWeb;
import org.quickweb.session.EditableParamScope;
import org.quickweb.template.TemplateExpr;
import org.quickweb.utils.*;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Handler {

    public static void insert(QuickModal quickModal, String table, String[] params) {
        String sql = SqlUtils.insert(table, params);
        handle(quickModal, sql, (conn, stmt) -> {
            new StmtHelper(quickModal.getQuickSession(), stmt).setParams(params);
            stmt.executeUpdate();
        });
    }

    public static void update(QuickModal quickModal, String table, String[] params, SqlParam sqlParam) {
        TemplateExpr whereExpr = new TemplateExpr(quickModal.getQuickSession(), sqlParam.getWhere());
        String sql = SqlUtils.update(table, params, whereExpr.getTemplate());

        handle(quickModal, sql, (conn, stmt) -> {
            StmtHelper stmtHelper = new StmtHelper(quickModal.getQuickSession(), stmt);
            stmtHelper.setParams(params);
            stmtHelper.setParams(whereExpr.getValues());
            stmt.executeUpdate();
        });
    }

    public static void delete(QuickModal quickModal, String table, SqlParam sqlParam) {
        TemplateExpr whereExpr = new TemplateExpr(quickModal.getQuickSession(), sqlParam.getWhere());
        String sql = SqlUtils.delete(table, whereExpr.getTemplate());

        handle(quickModal, sql, (conn, stmt) -> {
            StmtHelper stmtHelper = new StmtHelper(quickModal.getQuickSession(), stmt);
            stmtHelper.setParams(whereExpr.getValues());
            stmt.executeUpdate();
        });
    }

    public static void find(QuickModal quickModal, String paramName, String table, SqlParam sqlParam) {
        findAction(quickModal, paramName, table, sqlParam, (stmt, rs) -> {
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

    public static void findFirst(QuickModal quickModal, String paramName, String table, SqlParam sqlParam) {
        sqlParam = sqlParam.clone();
        sqlParam.setLimit(1);

        findAction(quickModal, paramName, table, sqlParam, (stmt, rs) -> {
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

    private static void findAction(QuickModal quickModal, String paramName, String table,
                                   SqlParam sqlParam, FindAction action) {
        RequireUtils.requireNotEmpty(paramName);
        RequireUtils.requireNotNull(sqlParam, action);

        String select = sqlParam.getSelect();
        if (StringUtils.isEmpty(select))
            select = "*";

        TemplateExpr selectExpr = new TemplateExpr(quickModal.getQuickSession(), select);
        TemplateExpr whereExpr = new TemplateExpr(quickModal.getQuickSession(), sqlParam.getWhere());
        TemplateExpr orderExpr = new TemplateExpr(quickModal.getQuickSession(), sqlParam.getOrder());
        String sql = SqlUtils.find(table, selectExpr.getTemplate(), whereExpr.getTemplate(),
                orderExpr.getTemplate(), sqlParam.getOffset(), sqlParam.getLimit());

        handle(quickModal, sql, (conn, stmt) -> {
            StmtHelper stmtHelper = new StmtHelper(quickModal.getQuickSession(), stmt);
            stmtHelper.setParams(selectExpr.getValues());
            stmtHelper.setParams(whereExpr.getValues());
            stmtHelper.setParams(orderExpr.getValues());

            ResultSet rs = stmt.executeQuery();
            action.act(stmt, rs);
        });
    }

    private static void handle(QuickModal quickModal, String sql, Action action) {
        RequireUtils.requireNotNull(quickModal);

        Connection transConn = quickModal.getQuickSession().getConnection();
        Connection conn = transConn;
        if (conn == null)
            conn = DBUtils.getConnection();
        PreparedStatement stmt = null;

        if (QuickWeb.getConfig().getDb().isShowSql())
            System.out.println(sql);

        try {
            if (transConn == null)
                conn.setAutoCommit(true);

            stmt = conn.prepareStatement(sql);
            action.act(conn, stmt);
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (transConn == null)
                DBUtils.close(conn);
            DBUtils.close(stmt);
        }
    }

    private interface Action {
        void act(Connection conn, PreparedStatement stmt) throws SQLException;
    }

    private interface FindAction {
        void act(PreparedStatement stmt, ResultSet rs) throws SQLException;
    }
}
