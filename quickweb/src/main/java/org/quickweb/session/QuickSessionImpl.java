package org.quickweb.session;

import org.quickweb.error.DefaultErrorHandler;
import org.quickweb.error.ErrorHandler;
import org.quickweb.exception.*;
import org.quickweb.modal.QuickModal;
import org.quickweb.modal.QuickModalProxy;
import org.quickweb.modal.StmtHelper;
import org.quickweb.modal.handler.DataHandler;
import org.quickweb.session.action.ExecSQLAction;
import org.quickweb.session.action.RequireEmptyAction;
import org.quickweb.session.action.RequireEqualsAction;
import org.quickweb.session.param.ParamGenerator;
import org.quickweb.session.param.ParamHelper;
import org.quickweb.session.param.ParamMapper;
import org.quickweb.session.param.ParamMemberHelper;
import org.quickweb.session.scope.EditableScope;
import org.quickweb.session.scope.Scope;
import org.quickweb.template.TemplateExpr;
import org.quickweb.utils.*;
import org.quickweb.view.QuickView;
import org.quickweb.view.QuickViewImpl;
import org.quickweb.modal.QuickModalImpl;
import org.quickweb.view.QuickViewProxy;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;

public class QuickSessionImpl implements QuickSession {
    private QuickSession quickSessionProxy;
    private HttpServletRequest request;
    private HttpServletResponse response;
    private Connection connection;
    private ErrorHandler errorHandler = DefaultErrorHandler.getInstance();
    private Map<String, Object> modalParamMap = new ConcurrentHashMap<>();
    private static Map<String, Object> applicationParamMap = new ConcurrentHashMap<>();

    public QuickSessionImpl(HttpServletRequest request, HttpServletResponse response) {
        RequireUtils.requireNotNull(request, response);
        this.request = request;
        this.response = response;
    }

    @Override
    public void initProxy(QuickSession quickSessionProxy) {
        RequireUtils.requireNotNull(quickSessionProxy);
        this.quickSessionProxy = quickSessionProxy;
    }

    @Override
    public HttpServletRequest getRequest() {
        return request;
    }

    @Override
    public HttpServletResponse getResponse() {
        return response;
    }

    @Override
    public Connection getConnection() {
        return connection;
    }

    @Override
    public Map<String, Object> getModalParamMap() {
        return modalParamMap;
    }

    @Override
    public Map<String, Object> getApplicationParamMap() {
        return applicationParamMap;
    }

    @Override
    public QuickSession watch(Consumer<QuickSession> consumer) {
        RequireUtils.requireNotNull(consumer);
        consumer.accept(quickSessionProxy);
        return quickSessionProxy;
    }

    @Override
    public QuickSession watchRequest(BiConsumer<HttpServletRequest, HttpServletResponse> watcher) {
        RequireUtils.requireNotNull(watcher);
        watcher.accept(request, response);
        return quickSessionProxy;
    }

    @Override
    public QuickSession onError(ErrorHandler handler) {
        this.errorHandler = handler;
        return quickSessionProxy;
    }

    @Override
    public QuickSession requireParamNotNull(String... names) {
        requireParamNotNull((paramName, quickSession) -> {
            throw new ParamNullException(paramName);
        }, names);
        return quickSessionProxy;
    }

    @Override
    public QuickSession requireParamNotNull(RequireEmptyAction act, String... names) {
        RequireUtils.requireNotNull(act);
        for (String n : names) {
            if (getParam(n) == null) {
                try {
                    act.act(n, quickSessionProxy);
                } catch (Exception e) {
                    error(e);
                }
                break;
            }
        }
        return quickSessionProxy;
    }

    @Override
    public QuickSession requireParamNotNull(Exception e, String... names) {
        requireParamNotNull((paramName, quickSession) -> { throw e; }, names);
        return quickSessionProxy;
    }

    @Override
    public QuickSession requireParamNotEmpty(String... names) {
        requireParamNotEmpty((paramName, quickSession) -> {
            throw new ParamEmptyException(paramName);
        }, names);
        return quickSessionProxy;
    }

    @Override
    public QuickSession requireParamNotEmpty(RequireEmptyAction act, String... names) {
        RequireUtils.requireNotNull(act);
        for (String n : names) {
            boolean isEmpty = false;
            Object value = getParam(n);

            if (value == null) {
                isEmpty = true;
            } else if (value instanceof String) {
                // String
                String str = (String) value;
                isEmpty = str.isEmpty();
            } else if (value instanceof Object[]) {
                // Object[]
                Object[] objs = (Object[]) value;
                isEmpty = objs.length == 0;
            } else if (value instanceof Collection) {
                // Collection
                Collection c = (Collection) value;
                isEmpty = c.isEmpty();
            } else if (value instanceof Map) {
                // Map
                Map map = (Map) value;
                isEmpty = map.isEmpty();
            }

            if (isEmpty) {
                try {
                    act.act(n, quickSessionProxy);
                } catch (Exception e) {
                    error(e);
                }
                break;
            }
        }
        return quickSessionProxy;
    }

    @Override
    public QuickSession requireParamNotEmpty(Exception e, String... names) {
        requireParamNotEmpty((paramName, quickSession) -> { throw e; }, names);
        return quickSessionProxy;
    }

    @Override
    public QuickSession requireParamEquals(String name, Object expectedValue) {
        requireParamEquals(name, expectedValue,
                (paramName, actualValue, expectedValue1, quickSession) -> {
            throw new ParamNotExpectedException(paramName, actualValue, expectedValue1);
        });
        return quickSessionProxy;
    }

    @Override
    public QuickSession requireParamEquals(String name, Object expectedValue,
                                           RequireEqualsAction act) {
        RequireUtils.requireNotNull(act);
        Object actualValue = getParam(name);
        if (!Objects.equals(actualValue, expectedValue)) {
            try {
                act.act(name, actualValue, expectedValue, quickSessionProxy);
            } catch (Exception e) {
                error(e);
            }
        }
        return quickSessionProxy;
    }

    @Override
    public QuickSession requireParamEquals(String name, Object expectedValue, Exception e) {
        requireParamEquals(name, expectedValue,
                (paramName, actualValue, expectedValue1, quickSession) -> { throw e; });
        return quickSessionProxy;
    }

    @Override
    public QuickSession requireParamEqualsWith(String name, String expectedName) {
        requireParamEqualsWith(name, expectedName,
                (paramName, actualValue, expectedValue, quickSession) -> {
            throw new ParamNotExpectedException(paramName, actualValue, expectedValue);
        });
        return quickSessionProxy;
    }

    @Override
    public QuickSession requireParamEqualsWith(String name, String expectedName, RequireEqualsAction act) {
        Object actualValue = getParam(name);
        Object expectedValue = getParam(expectedName);
        if (!Objects.equals(actualValue, expectedValue)) {
            try {
                act.act(name, actualValue, expectedValue, quickSessionProxy);
            } catch (Exception e) {
                error(e);
            }
        }
        return quickSessionProxy;
    }

    @Override
    public QuickSession requireParamEqualsWith(String name, String expectedName, Exception e) {
        requireParamEqualsWith(name, expectedName,
                (paramName, actualValue, expectedValue, quickSession) -> { throw e; });
        return quickSessionProxy;
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> T getParam(String name) {
        // 按照作用域的优先级排列
        Scope[] scopes = {
                Scope.CONTEXT, Scope.REQUEST, Scope.MODAL,
                Scope.SESSION, Scope.COOKIE, Scope.APPLICATION
        };

        ParamHelper helper = new ParamHelper(name);
        if (helper.getScope() == Scope.ALL) {
            for (Scope scope : scopes) {
                Object value = getParam(helper, scope);
                if (value != null)
                    return (T) value;
            }
        } else {
            return getParam(helper, helper.getScope());
        }
        return null;
    }

    @SuppressWarnings("unchecked")
    private <T> T getParam(ParamHelper paramHelper, Scope scope) {
        RequireUtils.requireNotNull(paramHelper, scope);

        String name = paramHelper.getParamName();
        Object value = null;
        switch (scope) {
            case CONTEXT:
                value = request.getAttribute(name);
                break;
            case MODAL:
                value = modalParamMap.get(name);
                break;
            case REQUEST:
                value = RequestUtils.getParam(request, name);
                break;
            case SESSION:
                value = SessionUtils.getAttribute(request, name);
                break;
            case COOKIE:
                value = CookieUtils.getValue(request, name);
                break;
            case APPLICATION:
                value = applicationParamMap.get(name);
                break;
            case ALL:
                value = getParam(name);
                break;
            default:
                ExceptionUtils.throwScopeNotMatchedException(scope);
        }
        return (T) ParamMemberHelper.getMemberValue(value, paramHelper);
    }

    @Override
    public QuickSession putParam(String name, Object value) {
        if (value != null) {
            ParamHelper helper = new ParamHelper(name);
            Scope scope = helper.getScope();
            EditableScope eScope;
            if (scope == Scope.ALL) {
                eScope = EditableScope.CONTEXT;
            } else {
                eScope = EditableScope.of(helper.getScope());
            }

            putParam(helper.getParamName(), value, eScope);
        }
        return quickSessionProxy;
    }

    private void putParam(String name, Object value, EditableScope scope) {
        RequireUtils.requireNotNull(name, scope);
        if (value != null) {
            switch (scope) {
                case CONTEXT:
                    request.setAttribute(name, value);
                    break;
                case MODAL:
                    modalParamMap.put(name, value);
                    break;
                case APPLICATION:
                    applicationParamMap.put(name, value);
                    break;
                default:
                    ExceptionUtils.throwScopeNotMatchedException(scope);
            }
        }
    }

    @Override
    public QuickSession putParamBy(String name, ParamGenerator generator) {
        RequireUtils.requireNotNull(generator);
        try {
            putParam(name, generator.generate(quickSessionProxy));
        } catch (Exception e) {
            error(e);
        }
        return quickSessionProxy;
    }

    @Override
    public QuickSession putParamFrom(String name, String fromName) {
        putParam(name, getParam(fromName));
        return quickSessionProxy;
    }

    @Override
    public QuickSession removeParam(String name) {
        Scope scope = new ParamHelper(name).getScope();
        removeParam(name, EditableScope.of(scope));
        return quickSessionProxy;
    }

    private void removeParam(String name, EditableScope scope) {
        RequireUtils.requireNotNull(name, scope);
        switch (scope) {
            case CONTEXT:
                request.removeAttribute(name);
                break;
            case MODAL:
                modalParamMap.remove(name);
                break;
            case APPLICATION:
                applicationParamMap.remove(name);
                break;
        }
    }

    @Override
    public QuickSession mapParam(String name, ParamMapper mapper) {
        RequireUtils.requireNotNull(mapper);
        Object value = getParam(name);
        try {
            putParam(name, mapper.map(value));
        } catch (Exception e) {
            error(e);
        }
        return quickSessionProxy;
    }

    @Override
    public QuickSession watchParam(String name, Consumer<Object> watcher) {
        RequireUtils.requireNotNull(watcher);
        watcher.accept(getParam(name));
        return quickSessionProxy;
    }

    @Override
    public QuickSession setSession(String name, Object value) {
        SessionUtils.setAttribute(request, TemplateExpr.getString(quickSessionProxy, name), value);
        return quickSessionProxy;
    }

    @Override
    public QuickSession setSessionBy(String name, ParamGenerator generator) {
        RequireUtils.requireNotNull(generator);
        try {
            setSession(TemplateExpr.getString(quickSessionProxy, name),
                    generator.generate(quickSessionProxy));
        } catch (Exception e) {
            error(e);
        }
        return quickSessionProxy;
    }

    @Override
    public QuickSession setSessionFrom(String name, String paramName) {
        setSession(TemplateExpr.getString(quickSessionProxy, name), getParam(paramName));
        return quickSessionProxy;
    }

    @Override
    public QuickSession removeSession(String name) {
        SessionUtils.removeAttribute(request, TemplateExpr.getString(quickSessionProxy, name));
        return quickSessionProxy;
    }

    @Override
    public QuickSession invalidateSession() {
        SessionUtils.invalidateSession(request);
        return quickSessionProxy;
    }

    @Override
    public QuickSession addCookie(String name, String value) {
        CookieUtils.addCookie(response,
                new Cookie(TemplateExpr.getString(quickSessionProxy, name), value));
        return quickSessionProxy;
    }

    @Override
    public QuickSession addCookie(Cookie cookie) {
        CookieUtils.addCookie(response, cookie);
        return quickSessionProxy;
    }

    @Override
    public QuickSession addCookieBy(String name, Function<QuickSession, String> generator) {
        RequireUtils.requireNotNull(generator);
        addCookie(TemplateExpr.getString(quickSessionProxy, name), generator.apply(quickSessionProxy));
        return quickSessionProxy;
    }

    @Override
    public QuickSession addCookieBy(Function<QuickSession, Cookie> generator) {
        RequireUtils.requireNotNull(generator);
        addCookie(generator.apply(quickSessionProxy));
        return quickSessionProxy;
    }

    @Override
    public QuickSession addCookieFrom(String name, String paramName) {
        addCookie(TemplateExpr.getString(quickSessionProxy, name), getParam(paramName));
        return quickSessionProxy;
    }

    @Override
    public QuickSession removeCookie(String name) {
        CookieUtils.deleteCookie(response, TemplateExpr.getString(quickSessionProxy, name));
        return quickSessionProxy;
    }

    @Override
    public QuickModal modal(String table) {
        return QuickModalProxy.of(new QuickModalImpl(quickSessionProxy, table));
    }

    @Override
    public QuickSession startTransaction() {
        connection = DBUtils.getConnection();
        if (connection != null) {
            try {
                connection.setAutoCommit(false);
            } catch (SQLException e) {
                error(e);
            }
        }
        return quickSessionProxy;
    }

    @Override
    public QuickSession endTransaction() {
        RequireUtils.requireNotNull(connection);
        try {
            connection.commit();
        } catch (SQLException e) {
            error(e);
        }
        DBUtils.close(connection);
        connection = null;
        return quickSessionProxy;
    }

    @Override
    public QuickSession setSavepoint() {
        RequireUtils.requireNotNull(connection);
        try {
            connection.setSavepoint();
        } catch (SQLException e) {
            error(e);
        }
        return quickSessionProxy;
    }

    @Override
    public QuickSession rollback() {
        RequireUtils.requireNotNull(connection);
        try {
            connection.rollback();
        } catch (SQLException e) {
            error(e);
        }
        return quickSessionProxy;
    }

    @Override
    public QuickSession commit() {
        RequireUtils.requireNotNull(connection);
        try {
            connection.commit();
        } catch (SQLException e) {
            error(e);
        }
        return quickSessionProxy;
    }

    @Override
    public QuickView view() {
        return QuickViewProxy.of(new QuickViewImpl(quickSessionProxy, request, response));
    }

    @Override
    public void view(String name) {
        view().view(name);
    }

    @Override
    public void viewPath(String path) {
        view().viewPath(path);
    }

    @Override
    public void error(Exception e) {
        if (errorHandler != null)
            errorHandler.onError(e, quickSessionProxy);
    }

    @Override
    public QuickSession execSQL(String sql) {
        return execSQL(sql, null);
    }

    @Override
    public QuickSession execSQL(String sql, ExecSQLAction action) {
        TemplateExpr expr = new TemplateExpr(quickSessionProxy, sql);
        try {
            DataHandler.handle(quickSessionProxy, expr.getTemplate(), (stmt) -> {
                new StmtHelper(quickSessionProxy, stmt).setParams(expr.getValues());
                stmt.execute();
                if (action != null)
                    action.exec(stmt, quickSessionProxy);
            });
        } catch (SQLException e) {
            error(e);
        }
        return quickSessionProxy;
    }
}
