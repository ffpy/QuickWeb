package org.quickweb;

import org.quickweb.session.QuickSession;
import org.quickweb.session.QuickSessionImpl;
import org.quickweb.session.QuickSessionProxy;
import org.quickweb.utils.ExceptionUtils;
import org.quickweb.utils.XMLUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.net.URL;

public class QuickWeb {
    private static QuickWebConfig config;

    public static void initConfig() {
        final String CONFIG_FILENAME = "quickweb.xml";
        initConfig(CONFIG_FILENAME);
    }

    public static void initConfig(String configFilename) {
        final String ROOT_NAME = "quickweb";

        URL resource = QuickWeb.class.getClassLoader().getResource(configFilename);
        if (resource == null)
            ExceptionUtils.throwException("can't find config file " + configFilename);
        config = XMLUtils.fromXML(resource, ROOT_NAME, QuickWebConfig.class);
        if (config == null)
            ExceptionUtils.throwException("parse config file " + configFilename + " fail");
    }

    public static QuickSession server(HttpServletRequest request, HttpServletResponse response) {
        if (config == null)
            initConfig();
        return QuickSessionProxy.of(new QuickSessionImpl(request, response));
    }

    public static QuickWebConfig getConfig() {
        return config;
    }
}
