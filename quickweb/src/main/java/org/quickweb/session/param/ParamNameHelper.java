package org.quickweb.session.param;

import org.quickweb.session.scope.Scope;
import org.quickweb.utils.ExceptionUtils;
import org.quickweb.utils.StringUtils;

public class ParamNameHelper {
    private String param;
    private String scopeName;
    private String paramName;
    private String members;
    private Scope scope;

    public ParamNameHelper(String param) {
        this.param = param;
        init();
    }

    public String getName() {
        return paramName;
    }

    public String getMembers() {
        return members;
    }

    public String getScopeName() {
        return scopeName;
    }

    public Scope getScope() {
        if (scope == null)
            scope = matchScope();
        return scope;
    }

    private void init() {
        scopeName = "";
        paramName = "";
        members = "";
        scope = null;
        members = null;

        if (param.contains(":")) {
            String[] split = StringUtils.split(param, ':');

            if (split.length == 1) {
                members = split[0];
            } else if (split.length == 2) {
                scopeName = split[0];
                members = split[1];
            } else {
                throw ExceptionUtils.formatIncorrect(param);
            }
        } else {
            members = param;
        }

        ParamMember member = findMember();
        if (member == null)
            throw ExceptionUtils.exception("get paramName fail");

        paramName = member.getMember();
        if (paramName == null)
            throw ExceptionUtils.exception("get paramName fail");
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
                throw ExceptionUtils.unknownScope(scopeName);
        }
    }

    public boolean hasMember() {
        return !StringUtils.isEmpty(members);
    }

    public ParamMember findMember() {
        if (StringUtils.isEmpty(members))
            return null;

        String member;
        boolean isArrayMember = false;
        char[] cs = members.toCharArray();
        int index, beginIndex = -1;
        for (index = 0; index < cs.length; index++) {
            if (cs[index] == '.') {
                beginIndex = index + 1;
                break;
            } else if (index != 0 && cs[index] == '[') {
                beginIndex = index;
                break;
            }
        }

        if (index < cs.length && beginIndex >= 0) {
            member = members.substring(0, index);
            members = members.substring(beginIndex, members.length());
        } else {
            member = members;
            members = "";
        }

        if (isArrayMember(member)) {
            member = member.substring(1, member.length() - 1);
            isArrayMember = true;
        }

        return new ParamMember(member, isArrayMember);
    }

    private static boolean isArrayMember(String member) {
        if (member.length() < 2)
            return false;
        char[] cs = member.toCharArray();
        return cs[0] == '[' && cs[cs.length - 1] == ']';
    }
}
