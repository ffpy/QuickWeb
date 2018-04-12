package org.quickweb.view;

import org.quickweb.session.QuickSession;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface QuickView {

    QuickSession getQuickSession();

    HttpServletRequest getRequest();

    HttpServletResponse getResponse();

    void view(String path);
}
