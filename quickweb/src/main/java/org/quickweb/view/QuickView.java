package org.quickweb.view;

import org.quickweb.session.QuickSession;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface QuickView {

    void initProxy(QuickView quickViewProxy);

    QuickSession getQuickSession();

    HttpServletRequest getRequest();

    HttpServletResponse getResponse();

    void view(String name);

    void viewPath(String path);
}
