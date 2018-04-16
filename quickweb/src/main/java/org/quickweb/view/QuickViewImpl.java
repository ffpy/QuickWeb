package org.quickweb.view;

import org.quickweb.QuickWeb;
import org.quickweb.QuickWebConfig;
import org.quickweb.session.QuickSession;
import org.quickweb.template.TemplateExpr;
import org.quickweb.utils.ExceptionUtils;
import org.quickweb.utils.RequestUtils;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.Optional;

public class QuickViewImpl implements QuickView {
    private QuickView quickViewProxy;
    private QuickSession quickSession;
    private HttpServletRequest request;
    private HttpServletResponse response;

    public QuickViewImpl(QuickSession quickSession,
                         HttpServletRequest request, HttpServletResponse response) {
        this.quickSession = quickSession;
        this.request = request;
        this.response = response;
    }

    @Override
    public void initProxy(QuickView quickViewProxy) {
        this.quickViewProxy = quickViewProxy;
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
        quickSession.end();

        name = TemplateExpr.getString(quickSession, name);
        QuickWebConfig.View view = QuickWeb.getConfig().getView();
        String path = view.getPrefix() + name + view.getSuffix();

        mergeParams();
        try {
            request.getRequestDispatcher(path).forward(request, response);
        } catch (ServletException | IOException e) {
            ExceptionUtils.throwException(e);
        }
    }

    @Override
    public void viewPath(String path) {
        try {
            response.sendRedirect(path);
        } catch (IOException e) {
            ExceptionUtils.throwException(e);
        }
    }

    private void mergeParams() {
        // modal
        quickSession.getModalParamMap().forEach(this::putParam);

        // request
        Enumeration<String> parameterNames = request.getParameterNames();
        while (parameterNames.hasMoreElements()) {
            String name = parameterNames.nextElement();
            putParam(name, RequestUtils.getParam(request, name));
        }

        // session
        Optional.ofNullable(request.getSession(false)).ifPresent(session -> {
            Enumeration<String> names = session.getAttributeNames();
            while (names.hasMoreElements()) {
                String name = names.nextElement();
                putParam(name, session.getAttribute(name));
            }
        });

        // cookie
        Arrays.stream(request.getCookies())
                .forEach(cookie -> putParam(cookie.getName(), cookie.getValue()));

        // application
        quickSession.getApplicationParamMap().forEach(this::putParam);
    }

    private void putParam(String name, Object value) {
        if (value != null && request.getAttribute(name) == null)
            request.setAttribute(name, value);
    }
}
