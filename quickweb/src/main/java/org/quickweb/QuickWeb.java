package org.quickweb;

import org.quickweb.config.QuickWebConfig;
import org.quickweb.session.QuickSession;
import org.quickweb.session.QuickSessionImpl;
import org.quickweb.utils.XMLUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.net.URL;

public class QuickWeb {
    private static QuickWebConfig config;

    public static void init() {
        final String CONFIG_FILENAME = "quickweb.xml";
        init(CONFIG_FILENAME);
    }

    public static void init(String configFilename) {
        final String ROOT_NAME = "quickweb";

        URL resource = QuickWeb.class.getClassLoader().getResource(configFilename);
        if (resource == null)
            throw new RuntimeException("can't find config file '" + configFilename + "'");
        config = XMLUtils.fromXML(resource, ROOT_NAME, QuickWebConfig.class);
        if (config == null)
            throw new RuntimeException("parse config file '" + configFilename + "' fail");
    }

    public static QuickSession server(HttpServletRequest request, HttpServletResponse response) {
        if (config == null)
            init();
        return new QuickSessionImpl(request, response);
    }

    public static QuickWebConfig getConfig() {
        return config;
    }
}
