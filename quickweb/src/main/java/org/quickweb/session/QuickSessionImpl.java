package org.quickweb.session;

import org.apache.commons.beanutils.BeanUtils;
import org.quickweb.session.error.DefaultErrorHandler;
import org.quickweb.session.error.ErrorHandler;
import org.quickweb.exception.*;
import org.quickweb.modal.QuickModal;
import org.quickweb.modal.QuickModalProxy;
import org.quickweb.modal.param.StmtHelper;
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
import java.lang.reflect.InvocationTargetException;
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
        this.request = request;
        this.response = response;
    }

    @Override
    public void initProxy(QuickSession quickSessionProxy) {
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
        consumer.accept(quickSessionProxy);
        return quickSessionProxy;
    }

    @Override
    public QuickSession watchRequest(BiConsumer<HttpServletRequest, HttpServletResponse> watcher) {
        watcher.accept(request, response);
        return quickSessionProxy;
    }

    @Override
    public QuickSession onError(ErrorHandler handler) {
        this.errorHandler = handler;
        return quickSessionProxy;
    }

    @Override
    public QuickSession requireParamNotNull(String... params) {
        requireParamNotNull((paramName, quickSession) -> {
            throw new ParamNullException(paramName);
        }, params);
        return quickSessionProxy;
    }

    @Override
    public QuickSession requireParamNotNull(RequireEmptyAction act, String... params) {
        for (String n : params) {
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
    public QuickSession requireParamNotNull(Exception e, String... params) {
        requireParamNotNull((paramName, quickSession) -> { throw e; }, params);
        return quickSessionProxy;
    }

    @Override
    public QuickSession requireParamNotEmpty(String... params) {
        requireParamNotEmpty((paramName, quickSession) -> {
            throw new ParamEmptyException(paramName);
        }, params);
        return quickSessionProxy;
    }

    @Override
    public QuickSession requireParamNotEmpty(RequireEmptyAction act, String... params) {
        for (String n : params) {
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
    public QuickSession requireParamNotEmpty(Exception e, String... params) {
        requireParamNotEmpty((paramName, quickSession) -> { throw e; }, params);
        return quickSessionProxy;
    }

    @Override
    public QuickSession requireParamEquals(String param, Object expectedValue) {
        requireParamEquals(param, expectedValue,
                (paramName, actualValue, expectedValue1, quickSession) -> {
            throw new ParamNotExpectedException(paramName, actualValue, expectedValue1);
        });
        return quickSessionProxy;
    }

    @Override
    public QuickSession requireParamEquals(String param, Object expectedValue,
                                           RequireEqualsAction act) {
        Object actualValue = getParam(param);
        if (!Objects.equals(actualValue, expectedValue)) {
            try {
                act.act(param, actualValue, expectedValue, quickSessionProxy);
            } catch (Exception e) {
                error(e);
            }
        }
        return quickSessionProxy;
    }

    @Override
    public QuickSession requireParamEquals(String param, Object expectedValue, Exception e) {
        requireParamEquals(param, expectedValue,
                (paramName, actualValue, expectedValue1, quickSession) -> { throw e; });
        return quickSessionProxy;
    }

    @Override
    public QuickSession requireParamEqualsWith(String param, String expectedName) {
        requireParamEqualsWith(param, expectedName,
                (paramName, actualValue, expectedValue, quickSession) -> {
            throw new ParamNotExpectedException(paramName, actualValue, expectedValue);
        });
        return quickSessionProxy;
    }

    @Override
    public QuickSession requireParamEqualsWith(String param, String expectedName, RequireEqualsAction act) {
        Object actualValue = getParam(param);
        Object expectedValue = getParam(expectedName);
        if (!Objects.equals(actualValue, expectedValue)) {
            try {
                act.act(param, actualValue, expectedValue, quickSessionProxy);
            } catch (Exception e) {
                error(e);
            }
        }
        return quickSessionProxy;
    }

    @Override
    public QuickSession requireParamEqualsWith(String param, String expectedName, Exception e) {
        requireParamEqualsWith(param, expectedName,
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
        String name = paramHelper.getName();
        Object value;
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
                throw ExceptionUtils.nonsupportScope(scope);
        }
        return (T) ParamMemberHelper.getMemberValue(value, paramHelper);
    }

    @Override
    public QuickSession setParam(String name, Object value) {
        if (value != null) {
            ParamHelper helper = new ParamHelper(name);
            Scope scope = helper.getScope();
            EditableScope eScope;
            if (scope == Scope.ALL) {
                eScope = EditableScope.CONTEXT;
            } else {
                eScope = EditableScope.of(helper.getScope());
            }

            setParam(helper.getName(), value, eScope);
        }
        return quickSessionProxy;
    }

    @Override
    public QuickSession setParam(String name, Object value, EditableScope scope) {
        if (value != null) {
            switch (scope) {
                case CONTEXT:
                    request.setAttribute(name, value);
                    break;
                case MODAL:
                    modalParamMap.put(name, value);
                    break;
                case SESSION:
                    SessionUtils.setAttribute(request, TemplateExpr.getString(quickSessionProxy, name), value);
                case APPLICATION:
                    applicationParamMap.put(name, value);
                    break;
                default:
                    throw ExceptionUtils.nonsupportScope(scope);
            }
        }
        return quickSessionProxy;
    }

    @Override
    public QuickSession setParamBy(String name, ParamGenerator generator) {
        try {
            setParam(name, generator.generate(quickSessionProxy));
        } catch (Exception e) {
            error(e);
        }
        return quickSessionProxy;
    }

    @Override
    public QuickSession setParamFrom(String name, String fromName) {
        setParam(name, getParam(fromName));
        return quickSessionProxy;
    }

    @Override
    public QuickSession removeParam(String name) {
        Scope scope = new ParamHelper(name).getScope();
        removeParam(name, EditableScope.of(scope));
        return quickSessionProxy;
    }

    private void removeParam(String name, EditableScope scope) {
        switch (scope) {
            case CONTEXT:
                request.removeAttribute(name);
                break;
            case MODAL:
                modalParamMap.remove(name);
                break;
            case SESSION:
                SessionUtils.removeAttribute(request, name);
            case APPLICATION:
                applicationParamMap.remove(name);
                break;
        }
    }

    @Override
    public QuickSession mapParam(String name, ParamMapper mapper) {
        Object value = getParam(name);
        try {
            setParam(name, mapper.map(value));
        } catch (Exception e) {
            error(e);
        }
        return quickSessionProxy;
    }

    @Override
    public QuickSession watchParam(String name, Consumer<Object> watcher) {
        watcher.accept(getParam(name));
        return quickSessionProxy;
    }

    @Override
    public QuickSession mergeParamsToBean(Class<?> beanType, String beanParam, String... params) {
        try {
            Object bean = beanType.newInstance();
            for (String p : params) {
                BeanUtils.setProperty(bean, p, getParam(p));
                removeParam(p);
            }
            setParam(beanParam, bean);
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException(e);
        }
        return quickSessionProxy;
    }

    @Override
    public QuickSession mergeParamsToMap(String mapParam, String... params) {
        Map<String, Object> map = new HashMap<>();
        for (String p : params) {
            map.put(p, getParam(p));
            removeParam(p);
        }
        setParam(mapParam, map);
        return quickSessionProxy;
    }

    @Override
    public QuickSession clearParams(EditableScope scope) {
        switch (scope) {
            case CONTEXT:
                request.getParameterMap().clear();
                break;
            case MODAL:
                modalParamMap.clear();
                break;
            case SESSION:
                SessionUtils.invalidateSession(request);
            case APPLICATION:
                applicationParamMap.clear();
                break;
        }
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
    public QuickSession addCookieBy(Function<QuickSession, Cookie> generator) {
        addCookie(generator.apply(quickSessionProxy));
        return quickSessionProxy;
    }

    @Override
    public QuickSession removeCookie(String name) {
        CookieUtils.deleteCookie(response, TemplateExpr.getString(quickSessionProxy, name));
        return quickSessionProxy;
    }

    @Override
    public QuickModal modal(String... tables) {
        return QuickModalProxy.of(new QuickModalImpl(quickSessionProxy, tables));
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
        try {
            connection.setSavepoint();
        } catch (SQLException e) {
            error(e);
        }
        return quickSessionProxy;
    }

    @Override
    public QuickSession rollback() {
        try {
            connection.rollback();
        } catch (SQLException e) {
            error(e);
        }
        return quickSessionProxy;
    }

    @Override
    public QuickSession commit() {
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
    public void redirect(String path) {
        view().redirect(path);
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
            DataHandler.handle(quickSessionProxy, expr.getPlaceholderString(), (stmt) -> {
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
