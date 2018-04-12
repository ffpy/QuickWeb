package org.quickweb.session;

import com.sun.istack.internal.Nullable;
import org.quickweb.modal.QuickModal;
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

    QuickSession requireParamNotNull(String name, ParamScope scope);

    QuickSession requireParamNotEmpty(String... names);

    QuickSession requireParamNotEmpty(ParamScope scope, String... names);

    QuickSession requireParamEquals(String name, @Nullable Object expectedValue);

    QuickSession requireParamEquals(String name, ParamScope scope, String expectedName,
                                        ParamScope expectedScope);

    QuickSession requireParamEqualsWith(String name, ParamScope expectedScope);

    QuickSession requireParamEqualsWith(String name, String expectedName, ParamScope expectedScope);

    <T> T getParam(String name);

    <T> T getParam(String name, ParamScope scope);

    QuickSession putParam(String name, Object value);

    QuickSession putParam(String name, Object value, EditableParamScope scope);

    QuickSession putParamBy(String name, Function<QuickSession, Object> generator);

    QuickSession putParamBy(String name, EditableParamScope scope,
                                Function<QuickSession, Object> generator);

    QuickSession putParamFrom(String name, ParamScope fromScope);

    QuickSession putParamFrom(String name, ParamScope fromScope, ParamScope toScope);

    QuickSession putParamFrom(String fromName, ParamScope fromScope,
                                  String toName, ParamScope toScope);

    QuickSession removeParam(String name, EditableParamScope scope);

    QuickSession mapParam(String name, Function<Object, Object> mapper, EditableParamScope scope);

    QuickSession watchParam(String name, Consumer<Object> watcher);

    QuickSession watchParam(String name, ParamScope scope, Consumer<Object> watcher);

    QuickModal modal(String table);

    QuickSession startTransaction();

    QuickSession endTransaction();

    QuickSession setSavepoint();

    QuickSession rollback();

    QuickSession commit();

    QuickView view();

    void view(String path);
}
