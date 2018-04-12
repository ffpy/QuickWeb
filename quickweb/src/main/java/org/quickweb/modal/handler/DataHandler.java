package org.quickweb.modal.handler;

import org.quickweb.QuickWeb;
import org.quickweb.modal.QuickModal;
import org.quickweb.utils.*;

import java.sql.*;

public class DataHandler {

    public static void handle(QuickModal quickModal, String sql, Action action) {
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
}
