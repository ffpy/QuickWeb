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
        exec(() -> InsertHandler.insert(quickModalProxy, columnAndParamNames, sqlParam));
        return quickSession;
    }

    @Override
    public QuickSession insert(CP... cps) {
        exec(() -> InsertHandler.insert(quickModalProxy, cps, sqlParam));
        return quickSession;
    }

    @Override
    public QuickSession update(String... columnAndParamNames) {
        exec(() -> UpdateHandler.update(quickModalProxy, columnAndParamNames, sqlParam));
        return quickSession;
    }

    @Override
    public QuickSession update(CP... cps) {
        exec(() -> UpdateHandler.update(quickModalProxy, cps, sqlParam));
        return quickSession;
    }

    @Override
    public QuickSession delete() {
        exec(() -> DeleteHandler.delete(quickModalProxy, sqlParam));
        return quickSession;
    }

    @Override
    public QuickSession findFirst(String paramName) {
        exec(() -> QueryHandler.findFirst(quickModalProxy, paramName, sqlParam));
        return quickSession;
    }

    @Override
    public QuickSession find(String paramName) {
        exec(() -> QueryHandler.find(quickModalProxy, paramName, sqlParam));
        return quickSession;
    }

    @Override
    public QuickSession find(OnQueryResult onQueryResult) {
        exec(() -> QueryHandler.find(quickModalProxy, sqlParam, onQueryResult));
        return quickSession;
    }

    @Override
    public QuickSession count(String paramName) {
        exec(() -> QueryHandler.count(quickModalProxy, paramName, sqlParam));
        return quickSession;
    }

    @Override
    public QuickSession avg(String paramName, String column) {
        exec(() -> QueryHandler.avg(quickModalProxy, paramName, column, sqlParam));
        return quickSession;
    }

    @Override
    public QuickSession max(String paramName, String column, ResultType resultType) {
        exec(() -> QueryHandler.max(quickModalProxy, paramName, column, sqlParam, resultType));
        return quickSession;
    }

    @Override
    public QuickSession min(String paramName, String column, ResultType resultType) {
        exec(() -> QueryHandler.min(quickModalProxy, paramName, column, sqlParam, resultType));
        return quickSession;
    }

    @Override
    public QuickSession sum(String paramName, String column, ResultType resultType) {
        exec(() -> QueryHandler.sum(quickModalProxy, paramName, column, sqlParam, resultType));
        return quickSession;
    }

    private void resetSqlParam() {
        sqlParam = new SqlParam();
    }

    private void exec(ExecAction action) {
        try {
            action.exec();
        } catch (SQLException e) {
            quickSession.error(e);
        }
        resetSqlParam();
    }

    private interface ExecAction {
        void exec() throws SQLException;
    }
}
