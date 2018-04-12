package org.quickweb.modal.handler;

import org.quickweb.modal.QuickModal;
import org.quickweb.modal.SqlParam;
import org.quickweb.modal.StmtHelper;
import org.quickweb.utils.SqlUtils;

public class InsertHandler {

    public static void insert(QuickModal quickModal, String[] params, SqlParam sqlParam) {
        String sql = SqlUtils.insert(sqlParam.getTable(), params);
        DataHandler.handle(quickModal, sql, (conn, stmt) -> {
            new StmtHelper(quickModal.getQuickSession(), stmt).setParams(params);
            stmt.executeUpdate();
        });
    }
}
