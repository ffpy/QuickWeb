package org.quickweb.modal;

import org.quickweb.QuickWeb;
import org.quickweb.template.TemplateExpr;
import org.quickweb.utils.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class Handler {

    private static void handle(QuickModal quickModal, String sql, Action action) {
        ObjectUtils.requireNotNull(quickModal);

        Connection transConn = quickModal.getQuickSession().getConnection();
        Connection conn = transConn;
        if (conn == null)
            conn = DBUtils.getConnection();
        PreparedStatement stmt = null;

        if (QuickWeb.getConfig().getDb().isShowSql())
            System.out.println(sql);

        try {
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

    public static void save(QuickModal quickModal, String table, String[] params) {
        handle(quickModal, SqlUtils.insert(table, params), (conn, stmt) -> {
            new StmtHelper(quickModal.getQuickSession(), stmt).setParams(params);
            stmt.executeUpdate();
        });
    }

    public static void update(QuickModal quickModal, String table, String[] params, String where) {
        TemplateExpr templateExpr = new TemplateExpr(quickModal.getQuickSession(), where);

        handle(quickModal, SqlUtils.update(table, params, templateExpr.getTemplate()), (conn, stmt) -> {
            StmtHelper stmtHelper = new StmtHelper(quickModal.getQuickSession(), stmt);
            stmtHelper.setParams(params);
            stmtHelper.setParams(templateExpr.getVarList());
            stmt.executeUpdate();
        });
    }

    private interface Action {
        void act(Connection conn, PreparedStatement stmt) throws SQLException;
    }
}
