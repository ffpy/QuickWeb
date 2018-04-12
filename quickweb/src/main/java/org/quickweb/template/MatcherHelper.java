package org.quickweb.template;

import org.quickweb.session.ParamScope;
import org.quickweb.session.QuickSession;
import org.quickweb.utils.ObjectUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MatcherHelper {
    public static final String PATTERN = "\\$((\\w+):)?([\\w_]+)";
    private static final Pattern COMPILE = Pattern.compile(PATTERN);
    private final Matcher matcher;
    private final QuickSession quickSession;

    public MatcherHelper(QuickSession quickSession, String template) {
        ObjectUtils.requireNotNull(template);

        this.quickSession = quickSession;
        this.matcher = COMPILE.matcher(template);
    }

    public boolean find() {
        return matcher.find();
    }

    public String getMatch() {
        return matcher.group();
    }

    public String getScopeName() {
        return matcher.group(2);
    }

    public String getName() {
        return matcher.group(3);
    }

    public ParamScope getScope(String scopeName) {
        if (scopeName == null)
            return null;

        ParamScope scope;
        switch (scopeName.toUpperCase()) {
            case "CT":
                scope = ParamScope.CONTEXT;
                break;
            case "ML":
                scope = ParamScope.MODAL;
                break;
            case "RT":
                scope = ParamScope.REQUEST;
                break;
            case "SN":
                scope = ParamScope.SESSION;
                break;
            case "CE":
                scope = ParamScope.COOKIE;
                break;
            case "AN":
                scope = ParamScope.APPLICATION;
                break;
            default:
                throw new RuntimeException("unknown scope of '" + scopeName + "'");
        }
        return scope;
    }

    public Object getValue() {
        ParamScope scope = getScope(getScopeName());
        if (scope == null)
            return quickSession.getParam(getName());
        else
            return quickSession.getParam(getName(), scope);
    }
}
