package org.quickweb.modal.handler;

import org.quickweb.modal.QuickModal;
import org.quickweb.modal.SqlParam;
import org.quickweb.modal.StmtHelper;
import org.quickweb.template.TemplateExpr;
import org.quickweb.utils.SqlUtils;

import java.sql.SQLException;

public class DeleteHandler {

    public static void delete(QuickModal quickModal, SqlParam sqlParam) throws SQLException {
        TemplateExpr whereExpr = new TemplateExpr(quickModal.getQuickSession(), sqlParam.getWhere());
        String sql = SqlUtils.delete(sqlParam.getTable(), whereExpr.getTemplate());

        DataHandler.handle(quickModal, sql, (conn, stmt) -> {
            StmtHelper stmtHelper = new StmtHelper(quickModal.getQuickSession(), stmt);
            stmtHelper.setParams(whereExpr.getValues());
            stmt.executeUpdate();
        });
    }
}
