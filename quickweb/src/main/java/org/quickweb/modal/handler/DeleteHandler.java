package org.quickweb.modal.handler;

import org.quickweb.QuickWeb;
import org.quickweb.modal.param.SqlParam;
import org.quickweb.modal.param.SqlParamHelper;
import org.quickweb.modal.param.StmtHelper;
import org.quickweb.session.QuickSession;

import java.sql.SQLException;

public class DeleteHandler {

    public static void delete(QuickSession quickSession, SqlParam sqlParam) throws SQLException {
        SqlParamHelper helper = new SqlParamHelper(quickSession, sqlParam);
        String sql = QuickWeb.getSqlBuilder().delete(helper);

        DataHandler.handle(quickSession, sql, (stmt) -> {
           new StmtHelper(quickSession, stmt)
                   .setParams(helper.getConditionValues());
            stmt.executeUpdate();
        });
    }
}
