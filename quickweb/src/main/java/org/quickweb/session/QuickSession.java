package org.quickweb.session;

import com.sun.istack.internal.Nullable;
import org.quickweb.exception.ErrorHandler;
import org.quickweb.modal.QuickModal;
import org.quickweb.session.scope.EditableScope;
import org.quickweb.session.scope.Scope;
import org.quickweb.view.QuickView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.sql.Connection;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Function;

public interface QuickSession {

    HttpServletRequest getRequest();

    void setRequest(HttpServletRequest request);

    HttpServletResponse getResponse();

    void setResponse(HttpServletResponse response);

    Connection getConnection();

    void setConnection(Connection connection);

    Map<String, Object> getModalParamMap();

    Map<String, Object> getApplicationParamMap();

    QuickSession watch(Consumer<QuickSession> consumer);

    QuickSession onError(@Nullable ErrorHandler handler);

    QuickSession requireParamNotNull(String name);

    QuickSession requireParamNotEmpty(String... names);

    QuickSession requireParamEquals(String name, @Nullable Object expectedValue);

    QuickSession requireParamEqualsWith(String name, String expectedName);

    <T> T getParam(String name);

    <T> T getParam(String name, Scope scope);

    QuickSession putParam(String name, Object value);

    QuickSession putParam(String name, Object value, EditableScope scope);

    QuickSession putParamBy(String name, Function<QuickSession, Object> generator);

    QuickSession putParamBy(String name, EditableScope scope,
                                Function<QuickSession, Object> generator);

    QuickSession putParamFrom(String name, String fromName);

    QuickSession removeParam(String name, EditableScope scope);

    QuickSession mapParam(String name, Function<Object, Object> mapper, EditableScope scope);

    QuickSession watchParam(String name, Consumer<Object> watcher);

    QuickModal modal(String table);

    QuickSession startTransaction();

    QuickSession endTransaction();

    QuickSession setSavepoint();

    QuickSession rollback();

    QuickSession commit();

    QuickView view();

    void view(String path);

    void error(@Nullable Exception e);
}
