package org.quickweb.utils;

import com.sun.istack.internal.Nullable;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

public class SessionUtils {

    public static void setAttribute(HttpServletRequest request, String name, @Nullable Object value) {
        HttpSession session = request.getSession(true);
        if (session == null)
            throw ExceptionUtils.exception("get session fail");
        else
            session.setAttribute(name, value);
    }

    @SuppressWarnings("unchecked")
    public static <T> T getAttribute(HttpServletRequest request, String name) {
        HttpSession session = request.getSession(false);
        if (session != null)
            return (T) session.getAttribute(name);
        return null;
    }

    public static void removeAttribute(HttpServletRequest request, String name) {
        HttpSession session = request.getSession(false);
        if (session != null)
            session.removeAttribute(name);
    }

    public static void invalidateSession(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session != null)
            session.invalidate();
    }
}
