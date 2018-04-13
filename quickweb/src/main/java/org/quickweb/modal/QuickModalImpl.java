package org.quickweb.modal;

import org.quickweb.modal.handler.*;
import org.quickweb.session.param.CP;
import org.quickweb.session.QuickSession;
import org.quickweb.template.TemplateExpr;
import org.quickweb.utils.RequireUtils;

public class QuickModalImpl implements QuickModal {
    private QuickSession quickSession;
    private SqlParam sqlParam = new SqlParam();

    public QuickModalImpl(QuickSession quickSession, String table) {
        RequireUtils.requireNotNull(table, quickSession);
        this.quickSession = quickSession;
        this.sqlParam.setTable(new TemplateExpr(quickSession, table).getString());
    }

    @Override
    public String getTable() {
        return sqlParam.getTable();
    }

    @Override
    public QuickSession getQuickSession() {
        return quickSession;
    }

    @Override
    public QuickModal select(String... columns) {
        this.sqlParam.setSelect(columns);
        return this;
    }

    @Override
    public QuickModal where(String condition) {
        RequireUtils.requireNotNull(condition);
        this.sqlParam.setWhere(condition);
        return this;
    }

    @Override
    public QuickModal order(String... columns) {
        this.sqlParam.setOrder(columns);
        return this;
    }

    @Override
    public QuickModal offset(int value) {
        this.sqlParam.setOffset(value);
        return this;
    }

    @Override
    public QuickModal limit(int value) {
        this.sqlParam.setLimit(value);
        return this;
    }

    @Override
    public QuickSession insert(String... columnAndParams) {
        InsertHandler.insert(this, columnAndParams, sqlParam);
        resetSqlParam();
        return quickSession;
    }

    @Override
    public QuickSession insert(CP... cps) {
        InsertHandler.insert(this, cps, sqlParam);
        resetSqlParam();
        return quickSession;
    }

    @Override
    public QuickSession update(String... columnAndParams) {
        UpdateHandler.update(this, columnAndParams, sqlParam);
        resetSqlParam();
        return quickSession;
    }

    @Override
    public QuickSession update(CP... cps) {
        UpdateHandler.update(this, cps, sqlParam);
        resetSqlParam();
        return quickSession;
    }

    @Override
    public QuickSession delete() {
        DeleteHandler.delete(this, sqlParam);
        resetSqlParam();
        return quickSession;
    }

    @Override
    public QuickSession findFirst(String param) {
        QueryHandler.findFirst(this, param, sqlParam);
        resetSqlParam();
        return quickSession;
    }

    @Override
    public QuickSession find(String param) {
        QueryHandler.find(this, param, sqlParam);
        resetSqlParam();
        return quickSession;
    }

    @Override
    public QuickSession count(String param) {
        QueryHandler.count(this, param, sqlParam);
        resetSqlParam();
        return quickSession;
    }

    @Override
    public QuickSession avg(String param, String column) {
        QueryHandler.avg(this, param, column, sqlParam);
        resetSqlParam();
        return quickSession;
    }

    @Override
    public QuickSession max(String param, String column, ResultType resultType) {
        QueryHandler.max(this, param, column, sqlParam, resultType);
        resetSqlParam();
        return quickSession;
    }

    @Override
    public QuickSession min(String param, String column, ResultType resultType) {
        QueryHandler.min(this, param, column, sqlParam, resultType);
        resetSqlParam();
        return quickSession;
    }

    @Override
    public QuickSession sum(String param, String column, ResultType resultType) {
        QueryHandler.sum(this, param, column, sqlParam, resultType);
        resetSqlParam();
        return quickSession;
    }

    private void resetSqlParam() {
        this.sqlParam = new SqlParam();
    }
}
