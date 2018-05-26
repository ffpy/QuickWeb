package org.quickweb.session;

import com.sun.istack.internal.Nullable;
import org.quickweb.session.error.ErrorHandler;
import org.quickweb.modal.QuickModal;
import org.quickweb.session.action.ExecSQLAction;
import org.quickweb.session.action.RequireEmptyAction;
import org.quickweb.session.action.RequireEqualsAction;
import org.quickweb.session.param.ParamGenerator;
import org.quickweb.session.param.ParamMapper;
import org.quickweb.session.scope.EditableScope;
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

    QuickSession requireParamNotNull(String... params);

    QuickSession requireParamNotNull(RequireEmptyAction act, String... params);

    QuickSession requireParamNotNull(Exception e, String... params);

    QuickSession requireParamNotEmpty(String... params);

    QuickSession requireParamNotEmpty(RequireEmptyAction act, String... params);

    QuickSession requireParamNotEmpty(Exception e, String... params);

    QuickSession requireParamEquals(String param, @Nullable Object expectedValue);

    QuickSession requireParamEquals(String param, @Nullable Object expectedValue,
                                    RequireEqualsAction act);

    QuickSession requireParamEquals(String param, @Nullable Object expectedValue, Exception e);

    QuickSession requireParamEqualsWith(String param, String expectedName);

    QuickSession requireParamEqualsWith(String param, String expectedName, RequireEqualsAction act);

    QuickSession requireParamEqualsWith(String param, String expectedName, Exception e);

    <T> T getParam(String name);

    QuickSession setParam(String name, Object value);

    QuickSession setParam(String name, Object value, EditableScope scope);

    QuickSession setParamBy(String name, ParamGenerator generator);

    QuickSession setParamFrom(String name, String fromName);

    QuickSession removeParam(String name);

    QuickSession mapParam(String name, ParamMapper mapper);

    QuickSession watchParam(String name, Consumer<Object> watcher);

    QuickSession mergeParamsToBean(Class<?> beanType, String beanParam, String... params);

    QuickSession mergeParamsToMap(String mapParam, String... params);

    QuickSession clearParams(EditableScope scope);

    QuickSession addCookie(String name, String value);

    QuickSession addCookie(Cookie cookie);

    QuickSession addCookieBy(Function<QuickSession, Cookie> generator);

    QuickSession removeCookie(String name);

    QuickModal modal(String... tables);

    QuickSession execSQL(String sql);

    QuickSession execSQL(String sql, @Nullable ExecSQLAction action);

    QuickSession startTransaction();

    QuickSession endTransaction();

    QuickSession setSavepoint();

    QuickSession rollback();

    QuickSession commit();

    QuickView view();

    void view(String name);

    void redirect(String path);

    void error(@Nullable Exception e);

    default boolean isEnd() { return false; }

    default void end() {}
}
