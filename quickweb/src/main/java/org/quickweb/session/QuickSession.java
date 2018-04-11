package org.quickweb.session;

import com.sun.istack.internal.NotNull;
import org.quickweb.utils.CookieUtils;
import org.quickweb.utils.ParamUtils;
import org.quickweb.utils.SessionUtils;
import org.quickweb.view.QuickView;
import org.quickweb.modal.QuickModal;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;
import java.util.function.Function;

public class QuickSession {
    private HttpServletRequest request;
    private HttpServletResponse response;
    private Map<String, Object> modalParamMap = new ConcurrentHashMap<>();
    private static Map<String, Object> applicationParamMap = new ConcurrentHashMap<>();

    public QuickSession(@NotNull HttpServletRequest request, @NotNull HttpServletResponse response) {
        this.request = request;
        this.response = response;
    }

    public HttpServletRequest getRequest() {
        return request;
    }

    public HttpServletResponse getResponse() {
        return response;
    }

    public Map<String, Object> getModalParamMap() {
        return modalParamMap;
    }

    public static Map<String, Object> getApplicationParamMap() {
        return applicationParamMap;
    }

    public QuickSession watch(@NotNull Consumer<QuickSession> consumer) {
        consumer.accept(this);
        return this;
    }

    public QuickSession requireParamNotNull(@NotNull String name, @NotNull ParamScope scope) {
        ParamUtils.requireNotNull(getParam(name, scope), name);
        return this;
    }

    public QuickSession requireParamNotEmpty(@NotNull String... names) {
        for (String n : names) {
            String value = ParamUtils.requireNotNull(getParam(n), n);
            ParamUtils.requireNotEmpty(value, n);
        }
        return this;
    }

    public QuickSession requireParamNotEmpty(@NotNull ParamScope scope, @NotNull String... names) {
        for (String n : names) {
            String value = ParamUtils.requireNotNull(getParam(n, scope), n);
            ParamUtils.requireNotEmpty(value, n);
        }
        return this;
    }

    public QuickSession requireParamEquals(@NotNull String name, Object expectedValue) {
        ParamUtils.requireEquals(name, expectedValue, getParam(name));
        return this;
    }

    public QuickSession requireParamEquals(
            @NotNull String name, @NotNull ParamScope scope,
            @NotNull String expectedName, @NotNull ParamScope expectedScope) {
        Object actualValue = getParam(name, scope);
        Object expectedValue = getParam(expectedName, expectedScope);
        ParamUtils.requireEquals(name, expectedValue, actualValue);
        return this;
    }

    public QuickSession requireParamEqualsWith(@NotNull String name, @NotNull ParamScope expectedScope) {
        requireParamEquals(name, getParam(name, expectedScope));
        return this;
    }

    public QuickSession requireParamEqualsWith(
            @NotNull String name, @NotNull String expectedName, @NotNull ParamScope expectedScope) {
        requireParamEquals(name, getParam(expectedName, expectedScope));
        return this;
    }

    @SuppressWarnings("unchecked")
    public <T> T getParam(@NotNull String name) {
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

    public <T> T getParam(@NotNull String name, @NotNull ParamScope scope) {
        switch (scope) {
            case CONTEXT:
                return (T) request.getAttribute(name);
            case MODAL:
                return (T) modalParamMap.get(name);
            case REQUEST:
                return (T) request.getParameter(name);
            case SESSION:
                return SessionUtils.getAttribute(request, name);
            case COOKIE:
                return (T) CookieUtils.getValue(request, name);
            case APPLICATION:
                return (T) applicationParamMap.get(name);
        }
        return null;
    }

    public QuickSession putParam(@NotNull String name, Object value) {
        putParam(name, value, EditableParamScope.CONTEXT);
        return this;
    }

    public QuickSession putParam(@NotNull String name, Object value, @NotNull EditableParamScope scope) {
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
        }
        return this;
    }

    public QuickSession putParamBy(
            @NotNull String name, @NotNull Function<QuickSession, Object> generator) {
        putParamBy(name, EditableParamScope.CONTEXT, generator);
        return this;
    }

    public QuickSession putParamBy(
            @NotNull String name, @NotNull EditableParamScope scope,
            @NotNull Function<QuickSession, Object> generator) {
        putParam(name, generator.apply(this), scope);
        return this;
    }

    public QuickSession putParamFrom(@NotNull String name, @NotNull ParamScope fromScope) {
        putParamFrom(name, fromScope, ParamScope.CONTEXT);
        return this;
    }

    public QuickSession putParamFrom(
            @NotNull String name, @NotNull ParamScope fromScope,
            @NotNull ParamScope toScope) {
        Object value = getParam(name, fromScope);
        if (value != null)
            putParam(name, value, EditableParamScope.of(toScope));
        return this;
    }

    public QuickSession putParamFrom(
            @NotNull String fromName, @NotNull ParamScope fromScope,
            @NotNull String toName, @NotNull ParamScope toScope) {
        Object value = getParam(fromName, fromScope);
        if (value != null)
            putParam(toName, value, EditableParamScope.of(toScope));
        return this;
    }

    public QuickSession removeParam(@NotNull String name, @NotNull EditableParamScope scope) {
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

    public QuickSession mapParam(
            @NotNull String name, @NotNull Function<Object, Object> mapper,
            @NotNull EditableParamScope scope) {
        Object value = getParam(name, ParamScope.of(scope));
        putParam(name, mapper.apply(value), scope);
        return this;
    }

    public QuickSession watchParam(@NotNull String name, @NotNull Consumer<Object> watcher) {
        watcher.accept(getParam(name));
        return this;
    }

    public QuickSession watchParam(
            @NotNull String name, @NotNull ParamScope scope, @NotNull Consumer<Object> watcher) {
        watcher.accept(getParam(name, scope));
        return this;
    }

    public QuickModal modal(@NotNull String table) {
        return new QuickModal(table, this);
    }

    public QuickView view() {
        return new QuickView(this, request, response);
    }

    public void view(@NotNull String path) {
        view().view(path);
    }
}
