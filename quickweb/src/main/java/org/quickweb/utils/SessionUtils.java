package org.quickweb.utils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

public class SessionUtils {

    public static void setAttribute(HttpServletRequest request, String name, Object value) {
        RequireUtils.requireNotNull(request, name);
        HttpSession session = request.getSession(false);
        if (session == null) return;
        session.setAttribute(name, value);
    }

    public static <T> T getAttribute(HttpServletRequest request, String name) {
        RequireUtils.requireNotNull(request, name);
        HttpSession session = request.getSession(false);
        if (session == null) return null;
        return (T) session.getAttribute(name);
    }

    public static void removeAttribute(HttpServletRequest request, String name) {
        RequireUtils.requireNotNull(request, name);
        HttpSession session = request.getSession(false);
        if (session == null) return;
        session.removeAttribute(name);
    }

    public static void invalidateSession(HttpServletRequest request) {
        RequireUtils.requireNotNull(request);
        HttpSession session = request.getSession(false);
        if (session == null) return;
        session.invalidate();
    }
}
