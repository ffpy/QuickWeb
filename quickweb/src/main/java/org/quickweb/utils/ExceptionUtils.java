package org.quickweb.utils;

import org.quickweb.QuickWebException;
import org.quickweb.session.scope.Scope;
import org.quickweb.session.scope.IScope;

public class ExceptionUtils {

    public static void throwNullException() {
        throwNullException("param");
    }

    public static void throwNullException(String paramName) {
        throwException(paramName + " can't be null");
    }

    public static void throwEmptyException() {
        throwEmptyException("param");
    }

    public static void throwEmptyException(String paramName) {
        throwException(paramName + " can't be empty");
    }

    public static void throwNotEqualsException(String paramName, Object expectedValue, Object actualValue) {
        throwException("the expected value of " + paramName +
                " is " + expectedValue + ", and the actual value is " + actualValue);
    }

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

    public static void throwException(String message) {
        throw new QuickWebException(message);
    }

    public static void throwException(Throwable throwable) {
        throw new QuickWebException(throwable);
    }
}
