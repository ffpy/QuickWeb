package org.quickweb.utils;

import org.quickweb.exception.QuickWebException;
import org.quickweb.session.scope.Scope;
import org.quickweb.session.scope.IScope;

public class ExceptionUtils {

    public static QuickWebException nonsupportScope(IScope scope) {
        return exception("scope of " + scope + " is nonsupport");
    }

    public static QuickWebException unknownScope(String scopeName) {
        return exception("unknown scope of " + scopeName);
    }

    public static QuickWebException notEditableParamScope(Scope scope) {
        return exception(scope + " is not EditableParamScope");
    }

    public static QuickWebException mustMoreThan(String param, int value) {
        return exception(param + " must be more than " + value);
    }

    public static QuickWebException formatIncorrect(String param) {
        return exception("the name format of " + param + " is incorrect");
    }

    public static QuickWebException unsupportedClass(Class<?> cls, String... expected) {
        return exception("unsupported class of " + cls +
                ", the expected are " + StringUtils.join(expected, ','));
    }

    public static QuickWebException paramNotExists(String param) {
        return exception("the param of " + param + " is not exists");
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
