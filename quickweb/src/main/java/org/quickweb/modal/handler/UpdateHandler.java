package org.quickweb.modal.handler;

import org.quickweb.modal.QuickModal;
import org.quickweb.modal.SqlParam;
import org.quickweb.modal.StmtHelper;
import org.quickweb.session.param.CP;
import org.quickweb.template.TemplateExpr;
import org.quickweb.utils.ColumnUtils;
import org.quickweb.utils.SqlUtils;

import java.sql.SQLException;

public class UpdateHandler {

    public static void update(QuickModal quickModal, String[] columnAndParams,
                              SqlParam sqlParam) throws SQLException {
        update(quickModal, ColumnUtils.getColumns(columnAndParams), columnAndParams, sqlParam);
    }

    public static void update(QuickModal quickModal, CP[] cps,
                              SqlParam sqlParam) throws SQLException {
        update(quickModal, CP.getColumns(cps), CP.getParamNames(cps), sqlParam);
    }

    public static void update(QuickModal quickModal, String[] columns, String[] params,
                              SqlParam sqlParam) throws SQLException {
        TemplateExpr whereExpr = new TemplateExpr(quickModal.getQuickSession(), sqlParam.getWhere());
        String sql = SqlUtils.update(sqlParam.getTable(), columns, whereExpr.getTemplate());

        DataHandler.handle(quickModal.getQuickSession(), sql, (stmt) -> {
            StmtHelper stmtHelper = new StmtHelper(quickModal.getQuickSession(), stmt);
            stmtHelper.setParams(params);
            stmtHelper.setParams(whereExpr.getValues());
            stmt.executeUpdate();
        });
    }
}
