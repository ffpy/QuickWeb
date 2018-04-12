package org.quickweb.session;

import com.sun.istack.internal.Nullable;
import org.quickweb.exception.ScopeNotMatchedException;
import org.quickweb.modal.QuickModal;
import org.quickweb.utils.*;
import org.quickweb.view.QuickView;
import org.quickweb.view.QuickViewImpl;
import org.quickweb.modal.QuickModalImpl;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;
import java.util.function.Function;

public class QuickSessionImpl implements QuickSession {
    private HttpServletRequest request;
    private HttpServletResponse response;
    private Connection connection;
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
    public void setRequest(HttpServletRequest request) {
        this.request = request;
    }

    @Override
    public HttpServletResponse getResponse() {
        return response;
    }

    @Override
    public void setResponse(HttpServletResponse response) {
        this.response = response;
    }

    @Override
    public Connection getConnection() {
        return connection;
    }

    @Override
    public void setConnection(Connection connection) {
        this.connection = connection;
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

        consumer.accept(this);
        return this;
    }

    @Override
    public QuickSession requireParamNotNull(String name, ParamScope scope) {
        ParamUtils.requireNotNull(getParam(name, scope), name);
        return this;
    }

    @Override
    public QuickSession requireParamNotEmpty(String... names) {
        RequireUtils.requireNotNull((Object) names);

        for (String n : names) {
            String value = ParamUtils.requireNotNull(getParam(n), n);
            ParamUtils.requireNotEmpty(value, n);
        }
        return this;
    }

    @Override
    public QuickSession requireParamNotEmpty(ParamScope scope, String... names) {
        RequireUtils.requireNotNull(scope, names);

        for (String n : names) {
            String value = ParamUtils.requireNotNull(getParam(n, scope), n);
            ParamUtils.requireNotEmpty(value, n);
        }
        return this;
    }

    @Override
    public QuickSession requireParamEquals(String name, @Nullable Object expectedValue) {
        RequireUtils.requireNotNull(name);

        ParamUtils.requireEquals(name, expectedValue, getParam(name));
        return this;
    }

    @Override
    public QuickSession requireParamEquals(
            String name, ParamScope scope, String expectedName, ParamScope expectedScope) {
        Object actualValue = getParam(name, scope);
        Object expectedValue = getParam(expectedName, expectedScope);
        ParamUtils.requireEquals(name, expectedValue, actualValue);
        return this;
    }

    @Override
    public QuickSession requireParamEqualsWith(String name, ParamScope expectedScope) {
        requireParamEquals(name, getParam(name, expectedScope));
        return this;
    }

    @Override
    public QuickSession requireParamEqualsWith(
            String name, String expectedName, ParamScope expectedScope) {
        requireParamEquals(name, getParam(expectedName, expectedScope));
        return this;
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> T getParam(String name) {
        // 按照作用域的优先级排列
        ParamScope[] scopes = {
                ParamScope.CONTEXT, ParamScope.MODAL, ParamScope.REQUEST,
                ParamScope.SESSION, ParamScope.COOKIE, ParamScope.APPLICATION
        };

        for (ParamScope scope : scopes) {
            Object value = getParam(name, scope);
            if (value != null)
                return (T) value;
        }
        return null;
    }

    @Override
    public <T> T getParam(String name, ParamScope scope) {
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
                throw new ScopeNotMatchedException(scope);
        }
    }

    @Override
    public QuickSession putParam(String name, Object value) {
        if (value != null)
            putParam(name, value, EditableParamScope.CONTEXT);
        return this;
    }

    @Override
    public QuickSession putParam(
            String name, Object value, EditableParamScope scope) {
        RequireUtils.requireNotNull(name, value, scope);

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
                throw new ScopeNotMatchedException(scope);
        }
        return this;
    }

    @Override
    public QuickSession putParamBy(
            String name, Function<QuickSession, Object> generator) {
        putParamBy(name, EditableParamScope.CONTEXT, generator);
        return this;
    }

    @Override
    public QuickSession putParamBy(
            String name, EditableParamScope scope,
            Function<QuickSession, Object> generator) {
        putParam(name, generator.apply(this), scope);
        return this;
    }

    @Override
    public QuickSession putParamFrom(String name, ParamScope fromScope) {
        putParamFrom(name, fromScope, ParamScope.CONTEXT);
        return this;
    }

    @Override
    public QuickSession putParamFrom(
            String name, ParamScope fromScope,
            ParamScope toScope) {
        Object value = getParam(name, fromScope);
        if (value != null)
            putParam(name, value, EditableParamScope.of(toScope));
        return this;
    }

    @Override
    public QuickSession putParamFrom(
            String fromName, ParamScope fromScope, String toName, ParamScope toScope) {
        Object value = getParam(fromName, fromScope);
        if (value != null)
            putParam(toName, value, EditableParamScope.of(toScope));
        return this;
    }

    @Override
    public QuickSession removeParam(String name, EditableParamScope scope) {
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
        return this;
    }

    @Override
    public QuickSession mapParam(
            String name, Function<Object, Object> mapper, EditableParamScope scope) {
        RequireUtils.requireNotNull(mapper);

        Object value = getParam(name, ParamScope.of(scope));
        putParam(name, mapper.apply(value), scope);
        return this;
    }

    @Override
    public QuickSession watchParam(String name, Consumer<Object> watcher) {
        RequireUtils.requireNotNull(watcher);

        watcher.accept(getParam(name));
        return this;
    }

    @Override
    public QuickSession watchParam(
            String name, ParamScope scope, Consumer<Object> watcher) {
        RequireUtils.requireNotNull(watcher);

        watcher.accept(getParam(name, scope));
        return this;
    }

    @Override
    public QuickModal modal(String table) {
        return new QuickModalImpl(this, table);
    }

    @Override
    public QuickSession startTransaction() {
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
    public void view(String path) {
        view().view(path);
    }
}
