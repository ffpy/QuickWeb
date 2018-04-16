package org.quickweb.modal.handler;

import org.quickweb.QuickWeb;
import org.quickweb.modal.param.SqlParam;
import org.quickweb.modal.param.SqlParamHelper;
import org.quickweb.modal.param.StmtHelper;
import org.quickweb.session.QuickSession;
import org.quickweb.session.param.CP;

import java.sql.SQLException;

public class InsertHandler {

    public static void insert(QuickSession quickSession, CP[] cps, SqlParam sqlParam) throws SQLException {
        insert(quickSession, CP.getColumns(cps), CP.getParamNames(cps), sqlParam);
    }

    public static void insert(QuickSession quickSession, String[] columns, String[] paramNames,
                              SqlParam sqlParam) throws SQLException {
        sqlParam.setColumns(columns);

        SqlParamHelper helper = new SqlParamHelper(quickSession, sqlParam);
        String sql = QuickWeb.getSqlBuilder().insert(helper);

        DataHandler.handle(quickSession, sql, (stmt) -> {
            new StmtHelper(quickSession, stmt)
                    .setParams(paramNames);
            stmt.executeUpdate();
        });
    }
}
