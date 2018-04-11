package org.quickweb;

import org.quickweb.session.QuickSession;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class QuickWeb {

    public static QuickSession server(HttpServletRequest request, HttpServletResponse response) {
        return new QuickSession(request, response);
    }
}
