package org.quickweb.modal;

import org.quickweb.modal.handler.*;
import org.quickweb.session.QuickSession;
import org.quickweb.template.TemplateExpr;
import org.quickweb.utils.RequireUtils;

public class QuickModal {
    private QuickSession quickSession;
    private SqlParam sqlParam = new SqlParam();

    public QuickModal(QuickSession quickSession, String table) {
        RequireUtils.requireNotNull(table, quickSession);
        this.quickSession = quickSession;
        this.sqlParam.setTable(new TemplateExpr(quickSession, table).getString());
    }

    public String getTable() {
        return sqlParam.getTable();
    }

    public QuickSession getQuickSession() {
        return quickSession;
    }

    public QuickModal select(String... columns) {
        this.sqlParam.setSelect(columns);
        return this;
    }

    public QuickModal where(String condition) {
        RequireUtils.requireNotNull(condition);
        this.sqlParam.setWhere(condition);
        return this;
    }

    public QuickModal order(String... columns) {
        this.sqlParam.setOrder(columns);
        return this;
    }

    public QuickModal offset(int value) {
        this.sqlParam.setOffset(value);
        return this;
    }

    public QuickModal limit(int value) {
        this.sqlParam.setLimit(value);
        return this;
    }

    public QuickSession insert(String... params) {
        InsertHandler.insert(this, params, sqlParam);
        resetSqlParam();
        return quickSession;
    }

    public QuickSession update(String... params) {
        UpdateHandler.update(this, params, sqlParam);
        resetSqlParam();
        return quickSession;
    }

    public QuickSession delete() {
        DeleteHandler.delete(this, sqlParam);
        resetSqlParam();
        return quickSession;
    }

    public QuickSession findFirst(String paramName) {
        QueryHandler.findFirst(this, paramName, sqlParam);
        resetSqlParam();
        return quickSession;
    }

    public QuickSession find(String paramName) {
        QueryHandler.find(this, paramName, sqlParam);
        resetSqlParam();
        return quickSession;
    }

    public QuickSession count(String paramName) {
        QueryHandler.count(this, paramName, sqlParam);
        resetSqlParam();
        return quickSession;
    }

    public QuickSession avg(String paramName, String column) {
        QueryHandler.avg(this, paramName, column, sqlParam);
        resetSqlParam();
        return quickSession;
    }

    public QuickSession max(String paramName, String column) {
        QueryHandler.max(this, paramName, column, sqlParam);
        resetSqlParam();
        return quickSession;
    }

    public QuickSession min(String paramName, String column) {
        QueryHandler.min(this, paramName, column, sqlParam);
        resetSqlParam();
        return quickSession;
    }

    public QuickSession sum(String paramName, String column) {
        QueryHandler.sum(this, paramName, column, sqlParam);
        resetSqlParam();
        return quickSession;
    }

    private void resetSqlParam() {
        this.sqlParam = new SqlParam();
    }
}
