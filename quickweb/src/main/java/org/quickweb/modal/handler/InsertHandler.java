package org.quickweb.modal.handler;

import org.quickweb.modal.QuickModal;
import org.quickweb.modal.SqlParam;
import org.quickweb.modal.StmtHelper;
import org.quickweb.session.param.CP;
import org.quickweb.utils.ColumnUtils;
import org.quickweb.utils.SqlUtils;

import java.sql.SQLException;

public class InsertHandler {

    public static void insert(QuickModal quickModal, String[] columnAndParams,
                              SqlParam sqlParam) throws SQLException {
        insert(quickModal, ColumnUtils.getColumns(columnAndParams), columnAndParams, sqlParam);
    }

    public static void insert(QuickModal quickModal, CP[] cps, SqlParam sqlParam) throws SQLException {
        insert(quickModal, CP.getColumns(cps), CP.getParamNames(cps), sqlParam);
    }

    public static void insert(QuickModal quickModal, String[] columns, String[] paramNames,
                              SqlParam sqlParam) throws SQLException {
        String sql = SqlUtils.insert(sqlParam.getTable(), columns);
        DataHandler.handle(quickModal, sql, (conn, stmt) -> {
            new StmtHelper(quickModal.getQuickSession(), stmt).setParams(paramNames);
            stmt.executeUpdate();
        });
    }
}
