package org.quickweb.session;

import com.sun.istack.internal.Nullable;
import org.apache.commons.lang3.StringUtils;
import org.quickweb.exception.*;
import org.quickweb.modal.QuickModal;
import org.quickweb.session.action.RequireEmptyAction;
import org.quickweb.session.action.RequireEqualsAction;
import org.quickweb.session.param.ParamGenerator;
import org.quickweb.session.param.ParamHelper;
import org.quickweb.session.param.ParamMapper;
import org.quickweb.session.scope.EditableScope;
import org.quickweb.session.scope.Scope;
import org.quickweb.template.TemplateExpr;
import org.quickweb.utils.*;
import org.quickweb.view.QuickView;
import org.quickweb.view.QuickViewImpl;
import org.quickweb.modal.QuickModalImpl;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;

public class QuickSessionImpl implements QuickSession {
    private HttpServletRequest request;
    private HttpServletResponse response;
    private Connection connection;
    private boolean isEnd = false;
    private ErrorHandler errorHandler = DefaultErrorHandler.getInstance();
    private Map<String, Object> modalParamMap = new ConcurrentHashMap<>();
    private static Map<String, Object> applicationParamMap = new ConcurrentHashMap<>();

    public QuickSessionImpl(HttpServletRequest request, HttpServletResponse response) {
        RequireUtils.requireNotNull(request, response);
        this.request = request;
        this.response = response;
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
        if (isEnd) return this;
        consumer.accept(this);
        return this;
    }

    @Override
    public QuickSession watchRequest(BiConsumer<HttpServletRequest, HttpServletResponse> watcher) {
        RequireUtils.requireNotNull(watcher);
        if (isEnd) return this;
        watcher.accept(request, response);
        return this;
    }

    @Override
    public QuickSession onError(@Nullable ErrorHandler handler) {
        if (isEnd) return this;
        this.errorHandler = handler;
        return this;
    }

    @Override
    public QuickSession requireParamNotNull(String name) {
        if (isEnd) return this;
        requireParamNotNull(name, (paramName, quickSession) -> {
            throw new ParamNullException(paramName);
        });
        return this;
    }

    @Override
    public QuickSession requireParamNotNull(String name, RequireEmptyAction act) {
        RequireUtils.requireNotNull(act);
        if (isEnd) return this;
        if (getParam(name) == null) {
            try {
                act.act(name, this);
            } catch (Exception e) {
                error(e);
            }
        }
        return this;
    }

    @Override
    public QuickSession requireParamNotEmpty(String... names) {
        if (isEnd) return this;
        requireParamNotEmpty((paramName, quickSession) -> {
            throw new ParamEmptyException(paramName);
        }, names);
        return this;
    }

    @Override
    public QuickSession requireParamNotEmpty(RequireEmptyAction act, String... names) {
        RequireUtils.requireNotNull(act);
        if (isEnd) return this;
        for (String n : names) {
            String value = getParam(n);
            if (StringUtils.isEmpty(value)) {
                try {
                    act.act(n, this);
                } catch (Exception e) {
                    error(e);
                }
                break;
            }
        }
        return this;
    }

    @Override
    public QuickSession requireParamEquals(String name, @Nullable Object expectedValue) {
        if (isEnd) return this;
        requireParamEquals(name, expectedValue, (paramName, actualValue, expectedValue1, quickSession) -> {
            throw new ParamNotEqualsException(paramName, actualValue, expectedValue1);
        });
        return this;
    }

    @Override
    public QuickSession requireParamEquals(String name, @Nullable Object expectedValue,
                                           RequireEqualsAction act) {
        RequireUtils.requireNotNull(act);
        if (isEnd) return this;
        Object actualValue = getParam(name);
        if (!Objects.equals(actualValue, expectedValue)) {
            try {
                act.act(name, actualValue, expectedValue, this);
            } catch (Exception e) {
                error(e);
            }
        }
        return this;
    }

    @Override
    public QuickSession requireParamEqualsWith(String name, String expectedName) {
        if (isEnd) return this;
        requireParamEqualsWith(name, expectedName, (paramName, actualValue, expectedValue, quickSession) -> {
            throw new ParamNotEqualsException(paramName, actualValue, expectedValue);
        });
        return this;
    }

    @Override
    public QuickSession requireParamEqualsWith(String name, String expectedName, RequireEqualsAction act) {
        if (isEnd) return this;
        Object actualValue = getParam(name);
        Object expectedValue = getParam(expectedName);
        if (!Objects.equals(actualValue, expectedValue)) {
            try {
                act.act(name, actualValue, expectedValue, this);
            } catch (Exception e) {
                error(e);
            }
        }
        return this;
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
                Object value = getParam(helper.getParamName(), scope);
                if (value != null)
                    return (T) value;
            }
        } else {
            return getParam(helper.getParamName(), helper.getScope());
        }
        return null;
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> T getParam(String name, Scope scope) {
        RequireUtils.requireNotNull(name, scope);

        switch (scope) {
            case CONTEXT:
                return (T) request.getAttribute(name);
            case MODAL:
                return (T) modalParamMap.get(name);
            case REQUEST:
                return (T) RequestUtils.getParam(request, name);
            case SESSION:
                return SessionUtils.getAttribute(request, name);
            case COOKIE:
                return (T) CookieUtils.getValue(request, name);
            case APPLICATION:
                return (T) applicationParamMap.get(name);
            case ALL:
                return getParam(name);
            default:
                ExceptionUtils.throwScopeNotMatchedException(scope);
        }
        return null;
    }

    @Override
    public QuickSession putParam(String name, Object value) {
        if (isEnd) return this;
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
        return this;
    }

    @Override
    public QuickSession putParam(String name, Object value, EditableScope scope) {
        if (isEnd) return this;
        if (value != null) {
            RequireUtils.requireNotNull(name, scope);
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
        return this;
    }

    @Override
    public QuickSession putParamBy(String name, ParamGenerator generator) {
        RequireUtils.requireNotNull(generator);
        if (isEnd) return this;
        putParam(name, generator.apply(this));
        return this;
    }

    @Override
    public QuickSession putParamFrom(String name, String fromName) {
        if (isEnd) return this;
        putParam(name, getParam(fromName));
        return this;
    }

    @Override
    public QuickSession removeParam(String name) {
        if (isEnd) return this;
        Scope scope = new ParamHelper(name).getScope();
        removeParam(name, EditableScope.of(scope));
        return this;
    }

    @Override
    public QuickSession removeParam(String name, EditableScope scope) {
        RequireUtils.requireNotNull(name, scope);
        if (isEnd) return this;
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
        return this;
    }

    @Override
    public QuickSession mapParam(String name, ParamMapper mapper) {
        if (isEnd) return this;
        Scope scope = new ParamHelper(name).getScope();
        mapParam(name, mapper, EditableScope.of(scope));
        return this;
    }

    @Override
    public QuickSession mapParam(String name, ParamMapper mapper, EditableScope scope) {
        RequireUtils.requireNotNull(mapper);
        if (isEnd) return this;
        Object value = getParam(name, Scope.of(scope));
        putParam(name, mapper.apply(value), scope);
        return this;
    }

    @Override
    public QuickSession watchParam(String name, Consumer<Object> watcher) {
        RequireUtils.requireNotNull(watcher);
        if (isEnd) return this;
        watcher.accept(getParam(name));
        return this;
    }

    @Override
    public QuickSession setSession(String name, Object value) {
        if (isEnd) return this;
        SessionUtils.setAttribute(request, TemplateExpr.getString(this, name), value);
        return this;
    }

    @Override
    public QuickSession setSessionBy(String name, ParamGenerator generator) {
        RequireUtils.requireNotNull(generator);
        if (isEnd) return this;
        setSession(TemplateExpr.getString(this, name), generator.apply(this));
        return this;
    }

    @Override
    public QuickSession setSessionFrom(String name, String paramName) {
        if (isEnd) return this;
        setSession(TemplateExpr.getString(this, name), getParam(paramName));
        return this;
    }

    @Override
    public QuickSession removeSession(String name) {
        if (isEnd) return this;
        SessionUtils.removeAttribute(request, TemplateExpr.getString(this, name));
        return this;
    }

    @Override
    public QuickSession invalidateSession() {
        if (isEnd) return this;
        SessionUtils.invalidateSession(request);
        return this;
    }

    @Override
    public QuickSession addCookie(String name, String value) {
        if (isEnd) return this;
        CookieUtils.addCookie(response,
                new Cookie(TemplateExpr.getString(this, name), value));
        return this;
    }

    @Override
    public QuickSession addCookie(Cookie cookie) {
        if (isEnd) return this;
        CookieUtils.addCookie(response, cookie);
        return this;
    }

    @Override
    public QuickSession addCookieBy(String name, Function<QuickSession, String> generator) {
        RequireUtils.requireNotNull(generator);
        if (isEnd) return this;
        addCookie(TemplateExpr.getString(this, name), generator.apply(this));
        return this;
    }

    @Override
    public QuickSession addCookieBy(Function<QuickSession, Cookie> generator) {
        RequireUtils.requireNotNull(generator);
        if (isEnd) return this;
        addCookie(generator.apply(this));
        return this;
    }

    @Override
    public QuickSession addCookieFrom(String name, String paramName) {
        if (isEnd) return this;
        addCookie(TemplateExpr.getString(this, name), getParam(paramName));
        return this;
    }

    @Override
    public QuickSession removeCookie(String name) {
        if (isEnd) return this;
        CookieUtils.deleteCookie(response, TemplateExpr.getString(this, name));
        return this;
    }

    @Override
    public QuickModal modal(String table) {
        return new QuickModalImpl(this, table);
    }

    @Override
    public QuickSession startTransaction() {
        if (isEnd) return this;
        connection = DBUtils.getConnection();
        if (connection != null) {
            try {
                connection.setAutoCommit(false);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return this;
    }

    @Override
    public QuickSession endTransaction() {
        if (isEnd) return this;
        RequireUtils.requireNotNull(connection);
        try {
            connection.commit();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        DBUtils.close(connection);
        connection = null;
        return this;
    }

    @Override
    public QuickSession setSavepoint() {
        if (isEnd) return this;
        RequireUtils.requireNotNull(connection);
        try {
            connection.setSavepoint();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return this;
    }

    @Override
    public QuickSession rollback() {
        if (isEnd) return this;
        RequireUtils.requireNotNull(connection);
        try {
            connection.rollback();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return this;
    }

    @Override
    public QuickSession commit() {
        if (isEnd) return this;
        RequireUtils.requireNotNull(connection);
        try {
            connection.commit();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return this;
    }

    @Override
    public QuickView view() {
        return new QuickViewImpl(this, request, response);
    }

    @Override
    public void view(String name) {
        if (isEnd) return;
        view().view(name);
    }

    @Override
    public void viewPath(String path) {
        if (isEnd) return;
        view().viewPath(path);
    }

    @Override
    public void error(@Nullable Exception e) {
        if (errorHandler != null)
            errorHandler.onError(e, this);
    }

    @Override
    public boolean isEnd() {
        return isEnd;
    }

    @Override
    public void end() {
        isEnd = true;
    }
}
