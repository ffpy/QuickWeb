package org.quickweb.session.param;

import org.apache.commons.lang3.StringUtils;
import org.quickweb.session.scope.Scope;
import org.quickweb.utils.ExceptionUtils;
import org.quickweb.utils.RequireUtils;

public class ParamHelper {
    private String param;
    private String scopeName;
    private String paramName;
    private String members;
    private Scope scope;

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

    public String getMembers() {
        return members;
    }

    public Scope getScope() {
        if (scope == null)
            scope = matchScope();
        return scope;
    }

    private void init() {
        scope = null;
        members = null;

        if (param.contains(":")) {
            String[] split = StringUtils.split(param, ':');
            if (split.length != 2)
                ExceptionUtils.throwFormatIncorrectException(param);

            scopeName = split[0];
            members = split[1];
        } else {
            members = param;
        }

        paramName = findMember();
        if (paramName == null)
            ExceptionUtils.throwException("get paramName fail");
    }

    private Scope matchScope() {
        if (scopeName == null)
            return Scope.ALL;

        switch (scopeName.toUpperCase()) {
            case "X":
            case "CONTEXT":
                return Scope.CONTEXT;
            case "M":
            case "MODAL":
                return Scope.MODAL;
            case "R":
            case "REQUEST":
                return Scope.REQUEST;
            case "S":
            case "SESSION":
                return Scope.SESSION;
            case "C":
            case "COOKIE":
                return Scope.COOKIE;
            case "A":
            case "APPLICATION":
                return Scope.APPLICATION;
            case "":
            case "ALL":
                return Scope.ALL;
            default:
                ExceptionUtils.throwUnknownScopeException(scopeName);
        }
        return null;
    }

    public boolean hasMember() {
        return !StringUtils.isEmpty(members);
    }

    public String findMember() {
        if (StringUtils.isEmpty(members))
            return null;

        String member;
        int index, beginIndex = -1;
        char[] cs = members.toCharArray();
        for (index = 0; index < cs.length; index++) {
            if (cs[index] == '.') {
                beginIndex = index + 1;
                break;
            } else if (index != 0 && cs[index] == '[') {
                beginIndex = index;
                break;
            }
        }

        if (index != cs.length && beginIndex >= 0) {
            member = members.substring(0, index);
            members = members.substring(beginIndex, members.length());
        } else {
            member = members;
            members = null;
        }

        return member;
    }
}
