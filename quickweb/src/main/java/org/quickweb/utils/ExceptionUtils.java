package org.quickweb.utils;

import org.quickweb.exception.QuickWebException;
import org.quickweb.session.scope.Scope;
import org.quickweb.session.scope.IScope;

public class ExceptionUtils {

    public static QuickWebException scopeNotMatched(IScope scope) {
        return exception("scope not matched, the value is " + scope);
    }

    public static QuickWebException unknownScope(String scopeName) {
        return exception("unknown scope of " + scopeName);
    }

    public static QuickWebException notEditableParamScope(Scope scope) {
        return exception(scope + " is not EditableParamScope");
    }

    public static QuickWebException mustMoreThan(String paramName, int value) {
        return exception(paramName + " must be more than " + value);
    }

    public static QuickWebException formatIncorrect(String param) {
        return exception("the name format of " + param + " is incorrect");
    }

    public static QuickWebException unsupportedClass(Class<?> cls, String... expected) {
        return exception("unsupported class of " + cls +
                ", the expected are " + StringUtils.join(expected, ','));
    }

    public static QuickWebException paramNotExists(String paramName) {
        return exception("the param of " + paramName + " is not exists");
    }

    public static QuickWebException exception(String message) {
        return new QuickWebException(message);
    }

    public static QuickWebException exception(Throwable throwable) {
        return new QuickWebException(throwable);
    }

    public static QuickWebException exception(String message, Throwable cause) {
        return new QuickWebException(message, cause);
    }
}
