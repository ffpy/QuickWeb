package org.quickweb.utils;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.Objects;

public class CookieUtils {

    public static Cookie getCookie(HttpServletRequest request, String name) {
        ObjectUtils.requireNonNull(request, name);

        return Arrays.stream(request.getCookies())
                .filter(cookie -> Objects.equals(cookie.getName(), name))
                .findFirst().orElse(null);
    }

    public static String getValue(HttpServletRequest request, String name) {
        ObjectUtils.requireNonNull(request, name);

        Cookie cookie = getCookie(request, name);
        if (cookie == null)
            return null;
        return cookie.getValue();
    }
}
