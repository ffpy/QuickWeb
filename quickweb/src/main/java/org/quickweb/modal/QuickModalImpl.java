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
    private QuickModal quickModalProxy;
    private QuickSession quickSession;
    private SqlParam sqlParam = new SqlParam();

    public QuickModalImpl(QuickSession quickSession, String table) {
        RequireUtils.requireNotNull(table, quickSession);
        this.quickSession = quickSession;
        this.sqlParam.setTable(TemplateExpr.getString(quickSession, table));
    }

    @Override
    public void initProxy(QuickModal quickModalProxy) {
        RequireUtils.requireNotNull(quickModalProxy);
        this.quickModalProxy = quickModalProxy;
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
        sqlParam.setSelect(columns);
        return quickModalProxy;
    }

    @Override
    public QuickModal selectFrom(String paramName) {
        Object value = quickSession.getParam(paramName);
        if (value == null)
            ExceptionUtils.throwParamNotExistsException(paramName);
        if (value instanceof String) {
            sqlParam.setSelect(StringUtils.split((String) value, ','));
        } else if (value instanceof String[]){
            sqlParam.setSelect((String[]) value);
        } else {
            ExceptionUtils.throwUnsupportedClassException(value.getClass(), "String", "String[]");
        }
        return quickModalProxy;
    }

    @Override
    public QuickModal where(String condition) {
        RequireUtils.requireNotNull(condition);
        sqlParam.setWhere(condition);
        return quickModalProxy;
    }

    @Override
    public QuickModal whereFrom(String paramName) {
        Object value = quickSession.getParam(paramName);
        if (value == null)
            ExceptionUtils.throwParamNotExistsException(paramName);
        if (value instanceof String) {
            sqlParam.setWhere((String) value);
        } else {
            ExceptionUtils.throwUnsupportedClassException(value.getClass(), "String");
        }
        return quickModalProxy;
    }

    @Override
    public QuickModal order(String... columns) {
        sqlParam.setOrder(columns);
        return quickModalProxy;
    }

    @Override
    public QuickModal orderFrom(String paramName) {
        Object value = quickSession.getParam(paramName);
        if (value == null)
            ExceptionUtils.throwParamNotExistsException(paramName);
        if (value instanceof String) {
            sqlParam.setOrder(StringUtils.split((String) value, ','));
        } else if (value instanceof String[]){
            sqlParam.setOrder((String[]) value);
        } else {
            ExceptionUtils.throwUnsupportedClassException(value.getClass(), "String", "String[]");
        }
        return quickModalProxy;
    }

    @Override
    public QuickModal offset(int value) {
        sqlParam.setOffset(value);
        return quickModalProxy;
    }

    @Override
    public QuickModal offsetFrom(String paramName) {
        Object value = quickSession.getParam(paramName);
        if (value == null)
            ExceptionUtils.throwParamNotExistsException(paramName);
        if (value instanceof Integer) {
            sqlParam.setOffset((Integer) value);
        } else {
            ExceptionUtils.throwUnsupportedClassException(value.getClass(), "Integer");
        }
        return quickModalProxy;
    }

    @Override
    public QuickModal limit(int value) {
        sqlParam.setLimit(value);
        return quickModalProxy;
    }

    @Override
    public QuickModal limitFrom(String paramName) {
        Object value = quickSession.getParam(paramName);
        if (value == null)
            ExceptionUtils.throwParamNotExistsException(paramName);
        if (value instanceof Integer) {
            sqlParam.setLimit((Integer) value);
        } else {
            ExceptionUtils.throwUnsupportedClassException(value.getClass(), "Integer");
        }
        return quickModalProxy;
    }

    @Override
    public QuickSession insert(String... columnAndParamNames) {
        try {
            InsertHandler.insert(quickModalProxy, columnAndParamNames, sqlParam);
        } catch (SQLException e) {
            quickSession.error(e);
        }
        resetSqlParam();
        return quickSession;
    }

    @Override
    public QuickSession insert(CP... cps) {
        try {
            InsertHandler.insert(quickModalProxy, cps, sqlParam);
        } catch (SQLException e) {
            quickSession.error(e);
        }
        resetSqlParam();
        return quickSession;
    }

    @Override
    public QuickSession update(String... columnAndParamNames) {
        try {
            UpdateHandler.update(quickModalProxy, columnAndParamNames, sqlParam);
        } catch (SQLException e) {
            quickSession.error(e);
        }
        resetSqlParam();
        return quickSession;
    }

    @Override
    public QuickSession update(CP... cps) {
        try {
            UpdateHandler.update(quickModalProxy, cps, sqlParam);
        } catch (SQLException e) {
            quickSession.error(e);
        }
        resetSqlParam();
        return quickSession;
    }

    @Override
    public QuickSession delete() {
        try {
            DeleteHandler.delete(quickModalProxy, sqlParam);
        } catch (SQLException e) {
            quickSession.error(e);
        }
        resetSqlParam();
        return quickSession;
    }

    @Override
    public QuickSession findFirst(String paramName) {
        try {
            QueryHandler.findFirst(quickModalProxy, paramName, sqlParam);
        } catch (SQLException e) {
            quickSession.error(e);
        }
        resetSqlParam();
        return quickSession;
    }

    @Override
    public QuickSession find(String paramName) {
        try {
            QueryHandler.find(quickModalProxy, paramName, sqlParam);
        } catch (SQLException e) {
            quickSession.error(e);
        }
        resetSqlParam();
        return quickSession;
    }

    @Override
    public QuickSession find(OnQueryResult onQueryResult) {
        try {
            QueryHandler.find(quickModalProxy, sqlParam, onQueryResult);
        } catch (SQLException e) {
            quickSession.error(e);
        }
        return quickSession;
    }

    @Override
    public QuickSession count(String paramName) {
        try {
            QueryHandler.count(quickModalProxy, paramName, sqlParam);
        } catch (SQLException e) {
            quickSession.error(e);
        }
        resetSqlParam();
        return quickSession;
    }

    @Override
    public QuickSession avg(String paramName, String column) {
        try {
            QueryHandler.avg(quickModalProxy, paramName, TemplateExpr.getString(quickSession, column),
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
            QueryHandler.max(quickModalProxy, paramName, TemplateExpr.getString(quickSession, column),
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
            QueryHandler.min(quickModalProxy, paramName, TemplateExpr.getString(quickSession, column),
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
            QueryHandler.sum(quickModalProxy, paramName, TemplateExpr.getString(quickSession, column),
                    sqlParam, resultType);
        } catch (SQLException e) {
            quickSession.error(e);
        }
        resetSqlParam();
        return quickSession;
    }

    private void resetSqlParam() {
        sqlParam = new SqlParam();
    }
}
