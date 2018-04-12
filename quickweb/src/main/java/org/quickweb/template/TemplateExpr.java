package org.quickweb.template;

import org.quickweb.session.QuickSession;
import org.quickweb.utils.ObjectUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TemplateExpr {
    private static final String pattern  = "\\$([\\w_])+";
    private static final Pattern compile = Pattern.compile(pattern);
    private QuickSession quickSession;
    private String template;

    public TemplateExpr(QuickSession quickSession, String template) {
        ObjectUtils.requireNotNull(quickSession, template);

        this.quickSession = quickSession;
        this.template = template;
    }

    public String getString() {
        Matcher matcher = compile.matcher(template);

        String s = template;
        while (matcher.find()) {
            String match = matcher.group();
            String name = matcher.group(1);
            Object value = quickSession.getParam(name);

            s = s.replace(match, value.toString());
        }
        return s;
    }

    public String getTemplate(String placeholder) {
        return template.replaceAll(pattern, placeholder);
    }

    public List<Object> getVarList() {
        Matcher matcher = compile.matcher(template);

        List<Object> list = new ArrayList<>();
        while (matcher.find()) {
            String name = matcher.group(1);
            Object value = quickSession.getParam(name);
            list.add(value);
        }
        return list;
    }

    public Map<String, Object> getVarMap() {
        Matcher matcher = compile.matcher(template);

        Map<String, Object> map = new HashMap<>();
        while (matcher.find()) {
            String name = matcher.group(1);
            Object value = quickSession.getParam(name);
            map.put(name, value);
        }
        return map;
    }
}
