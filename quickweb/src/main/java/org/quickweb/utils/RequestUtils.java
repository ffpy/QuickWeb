package org.quickweb.utils;

import org.quickweb.QuickWeb;

import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;

public class RequestUtils {

    public static String getParam(HttpServletRequest request, String name) {
        return getParam(request, name, QuickWeb.getConfig().getSession().getCharset());
    }

    public static String getParam(HttpServletRequest request, String name, String charset) {
        ObjectUtils.requireNotNull(request, name, charset);

        try {
            return convertCharset(request.getParameter(name), charset);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }

    private static String convertCharset(String s, String charset) throws UnsupportedEncodingException {
        return new String(s.getBytes("ISO-8859-1"), charset);
    }
}