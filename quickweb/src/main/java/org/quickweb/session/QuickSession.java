package org.quickweb.session;

import com.sun.istack.internal.Nullable;
import org.quickweb.exception.ErrorHandler;
import org.quickweb.modal.QuickModal;
import org.quickweb.session.action.RequireEmptyAction;
import org.quickweb.session.action.RequireEqualsAction;
import org.quickweb.session.param.ParamGenerator;
import org.quickweb.session.param.ParamMapper;
import org.quickweb.session.scope.EditableScope;
import org.quickweb.session.scope.Scope;
import org.quickweb.view.QuickView;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.sql.Connection;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;

public interface QuickSession {

    void initProxy(QuickSession quickSessionProxy);

    HttpServletRequest getRequest();

    HttpServletResponse getResponse();

    Connection getConnection();

    Map<String, Object> getModalParamMap();

    Map<String, Object> getApplicationParamMap();

    QuickSession watch(Consumer<QuickSession> consumer);

    QuickSession watchRequest(BiConsumer<HttpServletRequest, HttpServletResponse> watcher);

    QuickSession onError(@Nullable ErrorHandler handler);

    QuickSession requireParamNotNull(String... names);

    QuickSession requireParamNotNull(RequireEmptyAction act, String... names);

    QuickSession requireParamNotNull(Exception e, String... names);

    QuickSession requireParamNotEmpty(String... names);

    QuickSession requireParamNotEmpty(RequireEmptyAction act, String... names);

    QuickSession requireParamNotEmpty(Exception e, String... names);

    QuickSession requireParamEquals(String name, @Nullable Object expectedValue);

    QuickSession requireParamEquals(String name, @Nullable Object expectedValue,
                                    RequireEqualsAction act);

    QuickSession requireParamEquals(String name, @Nullable Object expectedValue, Exception e);

    QuickSession requireParamEqualsWith(String name, String expectedName);

    QuickSession requireParamEqualsWith(String name, String expectedName, RequireEqualsAction act);

    QuickSession requireParamEqualsWith(String name, String expectedName, Exception e);

    <T> T getParam(String name);

    <T> T getParam(String name, Scope scope);

    QuickSession putParam(String name, Object value);

    QuickSession putParam(String name, Object value, EditableScope scope);

    QuickSession putParamBy(String name, ParamGenerator generator);

    QuickSession putParamFrom(String name, String fromName);

    QuickSession removeParam(String name);

    QuickSession removeParam(String name, EditableScope scope);

    QuickSession mapParam(String name, ParamMapper mapper);

    QuickSession mapParam(String name, ParamMapper mapper, EditableScope scope);

    QuickSession watchParam(String name, Consumer<Object> watcher);

    QuickSession setSession(String name, Object value);

    QuickSession setSessionBy(String name, ParamGenerator generator);

    QuickSession setSessionFrom(String name, String paramName);

    QuickSession removeSession(String name);

    QuickSession invalidateSession();

    QuickSession addCookie(String name, String value);

    QuickSession addCookie(Cookie cookie);

    QuickSession addCookieBy(String name, Function<QuickSession, String> generator);

    QuickSession addCookieBy(Function<QuickSession, Cookie> generator);

    QuickSession addCookieFrom(String name, String paramName);

    QuickSession removeCookie(String name);

    QuickModal modal(String table);

    QuickSession startTransaction();

    QuickSession endTransaction();

    QuickSession setSavepoint();

    QuickSession rollback();

    QuickSession commit();

    QuickView view();

    void view(String name);

    void viewPath(String path);

    void error(@Nullable Exception e);

    default boolean isEnd() { return false; }

    default void end() {}
}
