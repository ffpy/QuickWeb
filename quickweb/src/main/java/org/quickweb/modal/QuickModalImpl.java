package org.quickweb.modal;

import org.quickweb.modal.handler.*;
import org.quickweb.session.param.CP;
import org.quickweb.session.QuickSession;
import org.quickweb.template.TemplateExpr;
import org.quickweb.utils.RequireUtils;

import java.sql.SQLException;

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
        try {
            InsertHandler.insert(this, columnAndParams, sqlParam);
        } catch (SQLException e) {
            quickSession.error(e);
        }
        resetSqlParam();
        return quickSession;
    }

    @Override
    public QuickSession insert(CP... cps) {
        try {
            InsertHandler.insert(this, cps, sqlParam);
        } catch (SQLException e) {
            quickSession.error(e);
        }
        resetSqlParam();
        return quickSession;
    }

    @Override
    public QuickSession update(String... columnAndParams) {
        try {
            UpdateHandler.update(this, columnAndParams, sqlParam);
        } catch (SQLException e) {
            quickSession.error(e);
        }
        resetSqlParam();
        return quickSession;
    }

    @Override
    public QuickSession update(CP... cps) {
        try {
            UpdateHandler.update(this, cps, sqlParam);
        } catch (SQLException e) {
            quickSession.error(e);
        }
        resetSqlParam();
        return quickSession;
    }

    @Override
    public QuickSession delete() {
        try {
            DeleteHandler.delete(this, sqlParam);
        } catch (SQLException e) {
            quickSession.error(e);
        }
        resetSqlParam();
        return quickSession;
    }

    @Override
    public QuickSession findFirst(String param) {
        try {
            QueryHandler.findFirst(this, param, sqlParam);
        } catch (SQLException e) {
            quickSession.error(e);
        }
        resetSqlParam();
        return quickSession;
    }

    @Override
    public QuickSession find(String param) {
        try {
            QueryHandler.find(this, param, sqlParam);
        } catch (SQLException e) {
            quickSession.error(e);
        }
        resetSqlParam();
        return quickSession;
    }

    @Override
    public QuickSession count(String param) {
        try {
            QueryHandler.count(this, param, sqlParam);
        } catch (SQLException e) {
            quickSession.error(e);
        }
        resetSqlParam();
        return quickSession;
    }

    @Override
    public QuickSession avg(String param, String column) {
        try {
            QueryHandler.avg(this, param, column, sqlParam);
        } catch (SQLException e) {
            quickSession.error(e);
        }
        resetSqlParam();
        return quickSession;
    }

    @Override
    public QuickSession max(String param, String column, ResultType resultType) {
        try {
            QueryHandler.max(this, param, column, sqlParam, resultType);
        } catch (SQLException e) {
            quickSession.error(e);
        }
        resetSqlParam();
        return quickSession;
    }

    @Override
    public QuickSession min(String param, String column, ResultType resultType) {
        try {
            QueryHandler.min(this, param, column, sqlParam, resultType);
        } catch (SQLException e) {
            quickSession.error(e);
        }
        resetSqlParam();
        return quickSession;
    }

    @Override
    public QuickSession sum(String param, String column, ResultType resultType) {
        try {
            QueryHandler.sum(this, param, column, sqlParam, resultType);
        } catch (SQLException e) {
            quickSession.error(e);
        }
        resetSqlParam();
        return quickSession;
    }

    private void resetSqlParam() {
        this.sqlParam = new SqlParam();
    }
}
