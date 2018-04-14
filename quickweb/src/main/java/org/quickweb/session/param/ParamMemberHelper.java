package org.quickweb.session.param;

import org.quickweb.utils.CharUtils;
import org.quickweb.utils.ExceptionUtils;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;

public class ParamMemberHelper {

    public static Object getMemberValue(Object value, ParamHelper paramHelper) {
        if (!paramHelper.hasMember())
            return value;

        String member = paramHelper.findMember();
        // a[1], a[2]
        if (isArrayMember(member)) {
            int index = 0;
            try {
                index = Integer.parseInt(
                        member.substring(1, member.length() - 1));
            } catch (NumberFormatException e) {
                ExceptionUtils.throwException("the index must be integer", e);
            }

            if (value instanceof Object[]) {
                Object[] objs = (Object[]) value;
                value = objs[index];
            } else if (value instanceof List) {
                List list = (List) value;
                value = list.get(index);
            }
        }
        // a.b, a.c
        else {
            if (value instanceof Map) {
                Map map = (Map) value;
                value = map.get(member);
            } else {
                Method method = null;
                String upperCaseMember = CharUtils.firstUpperCase(member);
                String getMethodName = "get" + upperCaseMember;
                String isMethodName = "is" + upperCaseMember;

                try {
                    method = value.getClass().getMethod(getMethodName);

                } catch (NoSuchMethodException ignored) {
                }

                if (method == null) {
                    try {
                        method = value.getClass().getMethod(isMethodName);
                    } catch (NoSuchMethodException ignored) {
                    }
                }

                if (method == null)
                    ExceptionUtils.throwException("the getter method of " + member + " is not found" +
                            ", the expected method is " + getMethodName + " or " + isMethodName);

                try {
                    value = method.invoke(value);
                } catch (IllegalAccessException | InvocationTargetException e) {
                    ExceptionUtils.throwException(e);
                }
            }
        }

        return getMemberValue(value, paramHelper);
    }

    public static boolean isArrayMember(String member) {
        if (member.length() < 3)
            return false;
        char[] cs = member.toCharArray();
        return cs[0] == '[' && cs[cs.length - 1] == ']';
    }
}
