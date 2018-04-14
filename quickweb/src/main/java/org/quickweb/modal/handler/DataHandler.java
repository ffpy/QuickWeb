package org.quickweb.modal.handler;

import org.quickweb.QuickWeb;
import org.quickweb.modal.QuickModal;
import org.quickweb.session.QuickSession;
import org.quickweb.utils.*;

import java.sql.*;

public class DataHandler {

    public static void handle(QuickSession quickSession, String sql, Action action) throws SQLException {
        RequireUtils.requireNotNull(quickSession);

        setQuickSessionUpdateCount(quickSession, null);
        setQuickSessionGeneratedKey(quickSession, null);

        Connection sessionConn = quickSession.getConnection();
        Connection conn = sessionConn;
        if (conn == null)
            conn = DBUtils.getConnection();
        PreparedStatement stmt = null;

        if (QuickWeb.getConfig().getDb().isShowSql())
            System.out.println(sql);

        try {
            if (sessionConn == null)
                conn.setAutoCommit(true);

            stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            action.act(stmt);

            setQuickSessionUpdateCount(quickSession, stmt);
            setQuickSessionGeneratedKey(quickSession, stmt);
        } finally {
            if (sessionConn == null)
                DBUtils.close(conn);
            DBUtils.close(stmt);
        }
    }

    private static void setQuickSessionUpdateCount(QuickSession quickSession,
                                                   Statement stmt) throws SQLException {
        Integer value = null;
        if (stmt != null)
            value = stmt.getUpdateCount();
        quickSession.putParam(QuickModal.UPDATE_COUNT, value);
    }

    private static void setQuickSessionGeneratedKey(QuickSession quickSession,
                                                    Statement stmt) throws SQLException {
        Object key = null;
        if (stmt != null) {
            ResultSet rs = stmt.getGeneratedKeys();
            if (rs.next())
                key = rs.getObject(1);
        }
        quickSession.putParam(QuickModal.GENERATED_KEY, key);
    }
}
