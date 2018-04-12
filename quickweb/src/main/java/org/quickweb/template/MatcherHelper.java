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
            case "X":
            case "CONTEXT":
                return ParamScope.CONTEXT;
            case "M":
            case "MODAL":
                return ParamScope.MODAL;
            case "R":
            case "REQUEST":
                return ParamScope.REQUEST;
            case "S":
            case "SESSION":
                return ParamScope.SESSION;
            case "C":
            case "COOKIE":
                return ParamScope.COOKIE;
            case "A":
            case "APPLICATION":
                return ParamScope.APPLICATION;
            case "":
            case "ALL":
                return ParamScope.ALL;
            default:
                throw new RuntimeException("unknown scope of " + scopeName);
        }
    }

    public Object getValue() {
        return quickSession.getParam(getName(), getScope(getScopeName()));
    }
}
