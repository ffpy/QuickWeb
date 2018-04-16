package org.quickweb.utils;

import org.apache.commons.lang3.StringUtils;
import org.quickweb.exception.QuickWebException;
import org.quickweb.session.scope.Scope;
import org.quickweb.session.scope.IScope;

public class ExceptionUtils {

    public static void throwScopeNotMatchedException(IScope scope) {
        throwException("scope not matched, the value is " + scope);
    }

    public static void throwUnknownScopeException(String scopeName) {
        throwException("unknown scope of " + scopeName);
    }

    public static void throwNotEditableParamScope(Scope scope) {
        throwException(scope + " is not EditableParamScope");
    }

    public static void throwMustMoreThanException(String paramName, int value) {
        throwException(paramName + " must be more than " + value);
    }

    public static void throwFormatIncorrectException(String param) {
        throwException("the name format of " + param + " is incorrect");
    }

    public static void throwUnsupportedClassException(Class<?> cls, String... expected) {
        throwException("unsupported class of " + cls +
                ", the expected are " + StringUtils.join(expected, ','));
    }

    public static void throwParamNotExistsException(String paramName) {
        throwException("the param of " + paramName + " is not exists");
    }

    public static void throwException(String message) {
        throw new QuickWebException(message);
    }

    public static void throwException(Throwable throwable) {
        throw new QuickWebException(throwable);
    }

    public static void throwException(String message, Throwable cause) {
        throw new QuickWebException(message, cause);
    }
}
