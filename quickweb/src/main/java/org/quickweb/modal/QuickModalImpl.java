package org.quickweb.modal;

import org.apache.commons.lang3.StringUtils;
import org.quickweb.modal.handler.*;
import org.quickweb.session.param.CP;
import org.quickweb.session.QuickSession;
import org.quickweb.template.TemplateExpr;
import org.quickweb.utils.ExceptionUtils;
import org.quickweb.utils.RequireUtils;

import java.sql.SQLException;

public class QuickModalImpl implements QuickModal {
    private QuickSession quickSession;
    private SqlParam sqlParam = new SqlParam();

    public QuickModalImpl(QuickSession quickSession, String table) {
        RequireUtils.requireNotNull(table, quickSession);
        this.quickSession = quickSession;
        this.sqlParam.setTable(TemplateExpr.getString(quickSession, table));
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
    public QuickModal selectFrom(String paramName) {
        Object value = quickSession.getParam(paramName);
        if (value == null)
            ExceptionUtils.throwParamNotExistsException(paramName);
        if (value instanceof String) {
            this.sqlParam.setSelect(StringUtils.split((String) value, ','));
        } else if (value instanceof String[]){
            this.sqlParam.setSelect((String[]) value);
        } else {
            ExceptionUtils.throwUnsupportedClassException(value.getClass(), "String", "String[]");
        }
        return this;
    }

    @Override
    public QuickModal where(String condition) {
        RequireUtils.requireNotNull(condition);
        this.sqlParam.setWhere(condition);
        return this;
    }

    @Override
    public QuickModal whereFrom(String paramName) {
        Object value = quickSession.getParam(paramName);
        if (value == null)
            ExceptionUtils.throwParamNotExistsException(paramName);
        if (value instanceof String) {
            this.sqlParam.setWhere((String) value);
        } else {
            ExceptionUtils.throwUnsupportedClassException(value.getClass(), "String");
        }
        return this;
    }

    @Override
    public QuickModal order(String... columns) {
        this.sqlParam.setOrder(columns);
        return this;
    }

    @Override
    public QuickModal orderFrom(String paramName) {
        Object value = quickSession.getParam(paramName);
        if (value == null)
            ExceptionUtils.throwParamNotExistsException(paramName);
        if (value instanceof String) {
            this.sqlParam.setOrder(StringUtils.split((String) value, ','));
        } else if (value instanceof String[]){
            this.sqlParam.setOrder((String[]) value);
        } else {
            ExceptionUtils.throwUnsupportedClassException(value.getClass(), "String", "String[]");
        }
        return this;
    }

    @Override
    public QuickModal offset(int value) {
        this.sqlParam.setOffset(value);
        return this;
    }

    @Override
    public QuickModal offsetFrom(String paramName) {
        Object value = quickSession.getParam(paramName);
        if (value == null)
            ExceptionUtils.throwParamNotExistsException(paramName);
        if (value instanceof Integer) {
            this.sqlParam.setOffset((Integer) value);
        } else {
            ExceptionUtils.throwUnsupportedClassException(value.getClass(), "Integer");
        }
        return this;
    }

    @Override
    public QuickModal limit(int value) {
        this.sqlParam.setLimit(value);
        return this;
    }

    @Override
    public QuickModal limitFrom(String paramName) {
        Object value = quickSession.getParam(paramName);
        if (value == null)
            ExceptionUtils.throwParamNotExistsException(paramName);
        if (value instanceof Integer) {
            this.sqlParam.setLimit((Integer) value);
        } else {
            ExceptionUtils.throwUnsupportedClassException(value.getClass(), "Integer");
        }
        return this;
    }

    @Override
    public QuickSession insert(String... columnAndParamNames) {
        try {
            InsertHandler.insert(this, columnAndParamNames, sqlParam);
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
    public QuickSession update(String... columnAndParamNames) {
        try {
            UpdateHandler.update(this, columnAndParamNames, sqlParam);
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
    public QuickSession findFirst(String paramName) {
        try {
            QueryHandler.findFirst(this, paramName, sqlParam);
        } catch (SQLException e) {
            quickSession.error(e);
        }
        resetSqlParam();
        return quickSession;
    }

    @Override
    public QuickSession find(String paramName) {
        try {
            QueryHandler.find(this, paramName, sqlParam);
        } catch (SQLException e) {
            quickSession.error(e);
        }
        resetSqlParam();
        return quickSession;
    }

    @Override
    public QuickSession count(String paramName) {
        try {
            QueryHandler.count(this, paramName, sqlParam);
        } catch (SQLException e) {
            quickSession.error(e);
        }
        resetSqlParam();
        return quickSession;
    }

    @Override
    public QuickSession avg(String paramName, String column) {
        try {
            QueryHandler.avg(this, paramName, TemplateExpr.getString(quickSession, column),
                    sqlParam);
        } catch (SQLException e) {
            quickSession.error(e);
        }
        resetSqlParam();
        return quickSession;
    }

    @Override
    public QuickSession max(String paramName, String column, ResultType resultType) {
        try {
            QueryHandler.max(this, paramName, TemplateExpr.getString(quickSession, column),
                    sqlParam, resultType);
        } catch (SQLException e) {
            quickSession.error(e);
        }
        resetSqlParam();
        return quickSession;
    }

    @Override
    public QuickSession min(String paramName, String column, ResultType resultType) {
        try {
            QueryHandler.min(this, paramName, TemplateExpr.getString(quickSession, column),
                    sqlParam, resultType);
        } catch (SQLException e) {
            quickSession.error(e);
        }
        resetSqlParam();
        return quickSession;
    }

    @Override
    public QuickSession sum(String paramName, String column, ResultType resultType) {
        try {
            QueryHandler.sum(this, paramName, TemplateExpr.getString(quickSession, column),
                    sqlParam, resultType);
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
