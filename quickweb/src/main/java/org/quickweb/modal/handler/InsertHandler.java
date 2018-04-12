package org.quickweb.modal.handler;

import org.quickweb.modal.QuickModal;
import org.quickweb.modal.SqlParam;
import org.quickweb.modal.StmtHelper;
import org.quickweb.session.CP;
import org.quickweb.utils.SqlUtils;

public class InsertHandler {

    public static void insert(QuickModal quickModal, String[] columnAndParams, SqlParam sqlParam) {
        insert(quickModal, columnAndParams, columnAndParams, sqlParam);
    }

    public static void insert(QuickModal quickModal, CP[] cps, SqlParam sqlParam) {
        insert(quickModal, CP.getColumns(cps), CP.getParams(cps), sqlParam);
    }

    public static void insert(QuickModal quickModal, String[] columns, String[] params, SqlParam sqlParam) {
        String sql = SqlUtils.insert(sqlParam.getTable(), columns);
        DataHandler.handle(quickModal, sql, (conn, stmt) -> {
            new StmtHelper(quickModal.getQuickSession(), stmt).setParams(params);
            stmt.executeUpdate();
        });
    }
}
