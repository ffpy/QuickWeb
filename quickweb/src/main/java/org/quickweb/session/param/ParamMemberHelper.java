package org.quickweb.session.param;

import org.quickweb.utils.ExceptionUtils;
import org.quickweb.utils.StringUtils;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;

public class ParamMemberHelper {

    public static Object getMemberValue(Object value, ParamHelper paramHelper) {
        if (value == null)
            return null;
        if (!paramHelper.hasMember())
            return value;

        ParamMember paramMember = paramHelper.findMember();
        String member = paramMember.getMember();

        if (value instanceof Object[] && paramMember.isArrayMember()) {
            // Object[]
            Object[] objs = (Object[]) value;
            value = objs[getIndexMember(member)];
        } else if (value instanceof List && paramMember.isArrayMember()) {
            // List
            List list = (List) value;
            value = list.get(getIndexMember(member));
        } else if (value instanceof Map) {
            // Map
            Map map = (Map) value;
            value = map.get(member);
        } else {
            // Object
            Method method = null;
            String upperCaseMember = StringUtils.upperCaseFirstChar(member);
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
                throw ExceptionUtils.exception("the getter method of " + member + " is not found" +
                        ", the expected method is " + getMethodName + " or " + isMethodName);

            try {
                value = method.invoke(value);
            } catch (IllegalAccessException | InvocationTargetException e) {
                throw ExceptionUtils.exception(e);
            }
        }

        return getMemberValue(value, paramHelper);
    }

    private static int getIndexMember(String member) {
        try {
            return Integer.parseInt(member);
        } catch (NumberFormatException e) {
            throw ExceptionUtils.exception("the index must be integer", e);
        }
    }
}
