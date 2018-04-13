package org.quickweb.view;

import com.sun.istack.internal.Nullable;
import org.quickweb.QuickWeb;
import org.quickweb.QuickWebConfig;
import org.quickweb.session.QuickSession;
import org.quickweb.template.TemplateExpr;
import org.quickweb.utils.ExceptionUtils;
import org.quickweb.utils.RequireUtils;
import org.quickweb.utils.RequestUtils;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.Optional;

public class QuickViewImpl implements QuickView {
    private QuickSession quickSession;
    private HttpServletRequest request;
    private HttpServletResponse response;

    public QuickViewImpl(QuickSession quickSession,
                         HttpServletRequest request, HttpServletResponse response) {
        RequireUtils.requireNotNull(quickSession, request, response);
        this.quickSession = quickSession;
        this.request = request;
        this.response = response;
    }

    @Override
    public QuickSession getQuickSession() {
        return quickSession;
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
    public void view(String name) {
        RequireUtils.requireNotNull(name);
        name = TemplateExpr.getString(quickSession, name);
        QuickWebConfig.View view = QuickWeb.getConfig().getView();
        String path = view.getPrefix() + name + view.getSuffix();
        viewPath(path);
    }

    @Override
    public void viewPath(String path) {
        RequireUtils.requireNotNull(path);

        mergeParams();
        try {
            request.getRequestDispatcher(path).forward(request, response);
        } catch (ServletException | IOException e) {
            ExceptionUtils.throwException(e);
        }
    }

    private void mergeParams() {
        quickSession.getModalParamMap().forEach(this::putParam);

        Enumeration<String> parameterNames = request.getParameterNames();
        while (parameterNames.hasMoreElements()) {
            String name = parameterNames.nextElement();
            putParam(name, RequestUtils.getParam(request, name));
        }

        Optional.ofNullable(request.getSession(false)).ifPresent(session -> {
            Enumeration<String> names = session.getAttributeNames();
            while (names.hasMoreElements()) {
                String name = names.nextElement();
                putParam(name, session.getAttribute(name));
            }
        });

        Arrays.stream(request.getCookies()).forEach(cookie -> putParam(cookie.getName(), cookie.getValue()));
    }

    private void putParam(String name, @Nullable Object value) {
        RequireUtils.requireNotNull(name);
        if (value != null && request.getAttribute(name) == null)
            request.setAttribute(name, value);
    }
}
