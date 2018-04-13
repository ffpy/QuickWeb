package org.quickweb.utils;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.Objects;

public class CookieUtils {

    public static void addCookie(HttpServletResponse response, Cookie cookie) {
        response.addCookie(cookie);
    }

    public static Cookie getCookie(HttpServletRequest request, String name) {
        RequireUtils.requireNotNull(request, name);
        return Arrays.stream(request.getCookies())
                .filter(cookie -> Objects.equals(cookie.getName(), name))
                .findFirst().orElse(null);
    }

    public static String getValue(HttpServletRequest request, String name) {
        RequireUtils.requireNotNull(request, name);
        Cookie cookie = getCookie(request, name);
        if (cookie == null)
            return null;
        return cookie.getValue();
    }

    public static void deleteCookie(HttpServletResponse response, String name) {
        Cookie cookie = new Cookie(name, null);
        cookie.setMaxAge(0);
        cookie.setPath("/");
        response.addCookie(cookie);
    }
}
