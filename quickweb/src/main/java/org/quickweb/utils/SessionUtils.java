package org.quickweb.utils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

public class SessionUtils {

    public static  <T> T getAttribute(HttpServletRequest request, String name) {
        RequireUtils.requireNotNull(request, name);

        HttpSession session = request.getSession(false);
        if (session == null) return null;
        return (T) session.getAttribute(name);
    }
}
