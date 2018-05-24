package org.quickweb.modal;

import org.quickweb.modal.handler.*;
import org.quickweb.modal.param.ResultType;
import org.quickweb.modal.param.SqlParam;
import org.quickweb.session.param.CP;
import org.quickweb.session.QuickSession;
import org.quickweb.utils.ExceptionUtils;
import org.quickweb.utils.StringUtils;

import java.sql.SQLException;

public class QuickModalImpl implements QuickModal {
    private QuickModal quickModalProxy;
    private QuickSession quickSession;
    private SqlParam sqlParam = new SqlParam();

    public QuickModalImpl(QuickSession quickSession, String[] tables) {
        this.quickSession = quickSession;
        this.sqlParam.setTables(tables);
    }

    @Override
    public void initProxy(QuickModal quickModalProxy) {
        this.quickModalProxy = quickModalProxy;
    }

    @Override
    public SqlParam getSqlParam() {
        return sqlParam;
    }

    @Override
    public QuickSession getQuickSession() {
        return quickSession;
    }

    @Override
    public QuickModal select(String... columns) {
        sqlParam.setColumns(columns);
        return quickModalProxy;
    }

    @Override
    public QuickModal selectFrom(String paramName) {
        Object value = quickSession.getParam(paramName);
        if (value == null)
            throw ExceptionUtils.paramNotExists(paramName);

        if (value instanceof String[]){
            sqlParam.setColumns((String[]) value);
        } else {
            throw ExceptionUtils.unsupportedClass(value.getClass(), "String[]");
        }
        return quickModalProxy;
    }

    @Override
    public QuickModal where(String condition) {
        sqlParam.setCondition(condition);
        return quickModalProxy;
    }

    @Override
    public QuickModal whereFrom(String paramName) {
        Object value = quickSession.getParam(paramName);
        if (value == null)
            throw ExceptionUtils.paramNotExists(paramName);
        if (value instanceof String) {
            sqlParam.setCondition((String) value);
        } else {
            throw ExceptionUtils.unsupportedClass(value.getClass(), "String");
        }
        return quickModalProxy;
    }

    @Override
    public QuickModal order(String... columns) {
        sqlParam.setOrders(columns);
        return quickModalProxy;
    }

    @Override
    public QuickModal orderFrom(String paramName) {
        Object value = quickSession.getParam(paramName);
        if (value == null)
            throw ExceptionUtils.paramNotExists(paramName);
        if (value instanceof String) {
            sqlParam.setOrders(StringUtils.split((String) value, ','));
        } else if (value instanceof String[]) {
            sqlParam.setOrders((String[]) value);
        } else {
            throw ExceptionUtils.unsupportedClass(value.getClass(), "String", "String[]");
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
            throw ExceptionUtils.paramNotExists(paramName);
        if (value instanceof Integer) {
            sqlParam.setOffset((Integer) value);
        } else {
            throw ExceptionUtils.unsupportedClass(value.getClass(), "Integer");
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
            throw ExceptionUtils.paramNotExists(paramName);
        if (value instanceof Integer) {
            sqlParam.setLimit((Integer) value);
        } else {
            throw ExceptionUtils.unsupportedClass(value.getClass(), "Integer");
        }
        return quickModalProxy;
    }

    @Override
    public QuickSession insert(String... columnAndParamNames) {
        insert(CP.of(columnAndParamNames));
        return quickSession;
    }

    @Override
    public QuickSession insert(CP... cps) {
        exec(() -> InsertHandler.insert(quickSession, cps, sqlParam));
        return quickSession;
    }

    @Override
    public QuickSession update(String... columnAndParamNames) {
        update(CP.of(columnAndParamNames));
        return quickSession;
    }

    @Override
    public QuickSession update(CP... cps) {
        exec(() -> UpdateHandler.update(quickSession, cps, sqlParam));
        return quickSession;
    }

    @Override
    public QuickSession delete() {
        exec(() -> DeleteHandler.delete(quickSession, sqlParam));
        return quickSession;
    }

    @Override
    public QuickSession findFirst(String paramName) {
        exec(() -> QueryHandler.findFirst(quickSession, paramName, sqlParam));
        return quickSession;
    }

    @Override
    public QuickSession find(String paramName) {
        exec(() -> QueryHandler.find(quickSession, paramName, sqlParam));
        return quickSession;
    }

    @Override
    public QuickSession find(OnQueryResult onQueryResult) {
        exec(() -> QueryHandler.find(quickSession, sqlParam, onQueryResult));
        return quickSession;
    }

    @Override
    public QuickSession count(String paramName) {
        exec(() -> QueryHandler.count(quickSession, paramName, sqlParam));
        return quickSession;
    }

    @Override
    public QuickSession avg(String paramName, String column) {
        exec(() -> QueryHandler.avg(quickSession, paramName, column, sqlParam));
        return quickSession;
    }

    @Override
    public QuickSession max(String paramName, String column, ResultType resultType) {
        exec(() -> QueryHandler.max(quickSession, paramName, column, sqlParam, resultType));
        return quickSession;
    }

    @Override
    public QuickSession min(String paramName, String column, ResultType resultType) {
        exec(() -> QueryHandler.min(quickSession, paramName, column, sqlParam, resultType));
        return quickSession;
    }

    @Override
    public QuickSession sum(String paramName, String column, ResultType resultType) {
        exec(() -> QueryHandler.sum(quickSession, paramName, column, sqlParam, resultType));
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
