package org.quickweb.view;

import com.sun.istack.internal.Nullable;
import org.quickweb.session.QuickSession;
import org.quickweb.utils.ObjectUtils;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.Optional;

public class QuickView {
    private QuickSession quickSession;
    private HttpServletRequest request;
    private HttpServletResponse response;

    public QuickView(
            QuickSession quickSession, HttpServletRequest request, HttpServletResponse response) {
        ObjectUtils.requireNonNull(quickSession, request, response);

        this.quickSession = quickSession;
        this.request = request;
        this.response = response;
    }

    public void view(String path) {
        ObjectUtils.requireNonNull(path);

        mergeParams();
        try {
            request.getRequestDispatcher(path).forward(request, response);
        } catch (ServletException | IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void mergeParams() {
        quickSession.getModalParamMap().forEach(this::putParam);
        request.getParameterMap().forEach(this::putParam);
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
        ObjectUtils.requireNonNull(name);

        if (value != null && request.getAttribute(name) == null)
            request.setAttribute(name, value);
    }
}
