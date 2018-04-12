package org.quickweb.modal.handler;

import org.quickweb.modal.QuickModal;
import org.quickweb.modal.SqlParam;
import org.quickweb.modal.StmtHelper;
import org.quickweb.session.CP;
import org.quickweb.template.TemplateExpr;
import org.quickweb.utils.SqlUtils;

public class UpdateHandler {

    public static void update(QuickModal quickModal, String[] columnAndParamNames, SqlParam sqlParam) {
        update(quickModal, columnAndParamNames, columnAndParamNames, sqlParam);
    }

    public static void update(QuickModal quickModal, CP[] cps, SqlParam sqlParam) {
        update(quickModal, CP.getColumns(cps), CP.getParams(cps), sqlParam);
    }

    public static void update(QuickModal quickModal, String[] columns, String[] params, SqlParam sqlParam) {
        TemplateExpr whereExpr = new TemplateExpr(quickModal.getQuickSession(), sqlParam.getWhere());
        String sql = SqlUtils.update(sqlParam.getTable(), columns, whereExpr.getTemplate());

        DataHandler.handle(quickModal, sql, (conn, stmt) -> {
            StmtHelper stmtHelper = new StmtHelper(quickModal.getQuickSession(), stmt);
            stmtHelper.setParams(params);
            stmtHelper.setParams(whereExpr.getValues());
            stmt.executeUpdate();
        });
    }
}
