package org.quickweb.utils;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;

import java.net.URL;

public class XMLUtils {

    @SuppressWarnings("unchecked")
    public static <T> T fromXML(URL url, String rootName, Class<T> resultType) {
        RequireUtils.requireNotNull(url, rootName, resultType);

        XStream xStream = new XStream(new DomDriver());
        XStream.setupDefaultSecurity(xStream);
        xStream.allowTypeHierarchy(resultType);
        xStream.alias(rootName, resultType);
        return (T) xStream.fromXML(url);
    }
}
