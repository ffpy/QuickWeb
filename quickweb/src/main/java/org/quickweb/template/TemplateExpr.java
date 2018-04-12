package org.quickweb.template;

import org.quickweb.session.QuickSession;
import org.quickweb.utils.ObjectUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TemplateExpr {
    private QuickSession quickSession;
    private String template;

    public TemplateExpr(QuickSession quickSession, String template) {
        ObjectUtils.requireNotNull(quickSession, template);

        this.quickSession = quickSession;
        this.template = template;
    }

    private MatcherHelper createMatcherHelper() {
        return new MatcherHelper(quickSession, template);
    }

    public String getString() {
        MatcherHelper matcherHelper = createMatcherHelper();
        String s = template;
        while (matcherHelper.find()) {
            s = s.replace(matcherHelper.getMatch(), String.valueOf(matcherHelper.getValue()));
        }
        return s;
    }

    public String getTemplate() {
        return getTemplate("?");
    }

    public String getTemplate(String placeholder) {
        return template.replaceAll(MatcherHelper.PATTERN, placeholder);
    }

    public List<Object> getVarList() {
        MatcherHelper matcherHelper = createMatcherHelper();
        List<Object> list = new ArrayList<>();
        while (matcherHelper.find()) {
            list.add(matcherHelper.getValue());
        }
        return list;
    }

    public Map<String, Object> getVarMap() {
        MatcherHelper matcherHelper = createMatcherHelper();
        Map<String, Object> map = new HashMap<>();
        while (matcherHelper.find()) {
            map.put(matcherHelper.getName(), matcherHelper.getValue());
        }
        return map;
    }
}
