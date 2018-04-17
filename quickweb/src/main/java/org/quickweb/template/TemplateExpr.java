package org.quickweb.template;

import org.quickweb.session.QuickSession;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TemplateExpr {
    private QuickSession quickSession;
    private String template;

    public TemplateExpr(QuickSession quickSession, String template) {
        this.quickSession = quickSession;
        this.template = template;
    }

    private MatcherHelper createMatcherHelper() {
        return new MatcherHelper(template);
    }

    public String getString() {
        MatcherHelper matcherHelper = createMatcherHelper();
        String s = template;
        while (matcherHelper.find()) {
            s = s.replace(matcherHelper.getMatch(),
                    String.valueOf(getValue(matcherHelper)));
        }
        return s;
    }

    public String getPlaceholderString() {
        return getPlaceholderString("?");
    }

    public String getPlaceholderString(String placeholder) {
        return template.replaceAll(MatcherHelper.PATTERN, placeholder);
    }

    public List<Object> getValues() {
        MatcherHelper matcherHelper = createMatcherHelper();
        List<Object> list = new ArrayList<>();
        while (matcherHelper.find()) {
            list.add(getValue(matcherHelper));
        }
        return list;
    }

    public Map<String, Object> getParamMap() {
        MatcherHelper matcherHelper = createMatcherHelper();
        Map<String, Object> map = new HashMap<>();
        while (matcherHelper.find()) {
            map.put(matcherHelper.getParam(), getValue(matcherHelper));
        }
        return map;
    }

    private Object getValue(MatcherHelper matcherHelper) {
        return quickSession.getParam(matcherHelper.getParam());
    }

    public static String getString(QuickSession quickSession, String template) {
        if (template == null)
            return null;
        if (template.isEmpty())
            return "";
        return new TemplateExpr(quickSession, template).getString();
    }
}
