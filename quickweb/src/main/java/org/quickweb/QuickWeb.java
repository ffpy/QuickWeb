package org.quickweb;

import org.quickweb.modal.bulider.MysqlBuilder;
import org.quickweb.modal.bulider.SqlBuilder;
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
    private static SqlBuilder sqlBuilder;

    public static void init() {
        final String CONFIG_FILENAME = "quickweb.xml";
        init(CONFIG_FILENAME);
    }

    public static void init(String configFilename) {
        final String ROOT_NAME = "quickweb";
        // 读取配置文件
        URL resource = QuickWeb.class.getClassLoader().getResource(configFilename);
        if (resource == null)
            throw ExceptionUtils.exception("config file " + configFilename + " not found");
        // 解析配置文件
        config = XMLUtils.fromXML(resource, ROOT_NAME, QuickWebConfig.class);
        if (config == null)
            throw ExceptionUtils.exception("parse config file " + configFilename + " fail");
        initSqlBuilder(config);
    }

    private static void initSqlBuilder(QuickWebConfig config) {
        String dialect = config.getDb().getDialect();
        switch (dialect.toUpperCase()) {
            case "MYSQL":
                sqlBuilder = new MysqlBuilder();
                break;
            default:
                throw ExceptionUtils.exception("unknown dialect of " + dialect);
        }
    }

    public static QuickSession server(HttpServletRequest request, HttpServletResponse response) {
        if (config == null)
            init();
        return QuickSessionProxy.of(new QuickSessionImpl(request, response));
    }

    public static QuickWebConfig getConfig() {
        return config;
    }

    public static SqlBuilder getSqlBuilder() {
        return sqlBuilder;
    }
}
