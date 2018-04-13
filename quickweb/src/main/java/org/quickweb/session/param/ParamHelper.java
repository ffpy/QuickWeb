package org.quickweb.session.param;

import org.apache.commons.lang3.StringUtils;
import org.quickweb.session.scope.ParamScope;
import org.quickweb.utils.RequireUtils;

public class ParamHelper {
    private String param;
    private String scopeName;
    private String paramName;
    private ParamScope scope;

    public ParamHelper() {
    }

    public ParamHelper(String param) {
        RequireUtils.requireNotEmpty(param);
        this.param = param;
        init();
    }

    public void setParam(String param) {
        this.param = param;
        init();
    }

    public String getScopeName() {
        return scopeName;
    }

    public String getParamName() {
        return paramName;
    }

    public ParamScope getScope() {
        if (scope == null)
            scope = matchScope();
        return scope;
    }

    private void init() {
        scope = null;
        if (param.contains(":")) {
            String[] split = StringUtils.split(param, ':');
            if (split.length != 2)
                throw new RuntimeException("the name format of " + param + " is incorrect");

            scopeName = split[0];
            paramName = split[1];
        } else {
            paramName = param;
        }
    }

    private ParamScope matchScope() {
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
}
