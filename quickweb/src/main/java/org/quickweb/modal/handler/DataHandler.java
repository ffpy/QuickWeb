package org.quickweb.modal.handler;

import org.quickweb.QuickWeb;
import org.quickweb.modal.QuickModal;
import org.quickweb.session.QuickSession;
import org.quickweb.utils.*;

import java.sql.*;

public class DataHandler {

    /**
     * 处理SQL语句
     * @param quickSession
     * @param sql
     * @param action
     * @throws SQLException
     */
    public static void handle(QuickSession quickSession, String sql, Action action) throws SQLException {
        // 设置为空值
        setQuickSessionUpdateCount(quickSession, null);
        setQuickSessionGeneratedKey(quickSession, null);

        // 获取全局Connection
        Connection sessionConn = quickSession.getConnection();

        Connection conn = sessionConn;
        PreparedStatement stmt = null;

        // 单独获取Connection
        if (conn == null)
            conn = DBUtils.getConnection();

        // 显示SQL语句
        if (QuickWeb.getConfig().getDb().isShowSql())
            System.out.println(sql);

        try {
            if (sessionConn == null)
                conn.setAutoCommit(true);

            stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            // 处理Statement
            action.act(stmt);

            // 更新值
            setQuickSessionUpdateCount(quickSession, stmt);
            setQuickSessionGeneratedKey(quickSession, stmt);
        } finally {
            // 关闭连接
            if (sessionConn == null)
                DBUtils.close(conn);
            DBUtils.close(stmt);
        }
    }

    /**
     * 设置执行Statement后的UpdateCount值
     * @param quickSession
     * @param stmt
     * @throws SQLException
     */
    private static void setQuickSessionUpdateCount(QuickSession quickSession,
                                                   Statement stmt) throws SQLException {
        Integer value = null;
        if (stmt != null)
            value = stmt.getUpdateCount();
        quickSession.putParam(QuickModal.UPDATE_COUNT, value);
    }

    /**
     * 设置执行Statement后的主键值
     * @param quickSession
     * @param stmt
     * @throws SQLException
     */
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
