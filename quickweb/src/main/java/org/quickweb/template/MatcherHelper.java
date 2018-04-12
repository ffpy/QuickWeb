package org.quickweb.template;

import org.quickweb.session.ParamScope;
import org.quickweb.session.QuickSession;
import org.quickweb.utils.RequireUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MatcherHelper {
    public static final String PATTERN = "\\$((\\w+):)?([\\w_]+)";
    private static final Pattern COMPILE = Pattern.compile(PATTERN);
    private final Matcher matcher;
    private final QuickSession quickSession;

    public MatcherHelper(QuickSession quickSession, String template) {
        RequireUtils.requireNotNull(template);

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
            return ParamScope.ALL;

        switch (scopeName.toUpperCase()) {
            case "CT":
                return ParamScope.CONTEXT;
            case "ML":
                return ParamScope.MODAL;
            case "RT":
                return ParamScope.REQUEST;
            case "SN":
                return ParamScope.SESSION;
            case "CE":
                return ParamScope.COOKIE;
            case "AN":
                return ParamScope.APPLICATION;
            case "AL":
                return ParamScope.ALL;
            default:
                throw new RuntimeException("unknown scope of '" + scopeName + "'");
        }
    }

    public Object getValue() {
        return quickSession.getParam(getName(), getScope(getScopeName()));
    }
}
