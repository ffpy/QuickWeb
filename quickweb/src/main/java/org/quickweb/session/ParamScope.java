package org.quickweb.session;

import org.quickweb.exception.ScopeNotMatchedException;
import org.quickweb.utils.RequireUtils;

public enum ParamScope implements Scope {
    CONTEXT,
    MODAL,
    REQUEST,
    SESSION,
    COOKIE,
    APPLICATION,
    ALL,
    ;

    @SuppressWarnings("Duplicates")
    public static ParamScope of(EditableParamScope scope) {
        RequireUtils.requireNotNull(scope);

        switch (scope) {
            case CONTEXT:
                return CONTEXT;
            case MODAL:
                return MODAL;
            case APPLICATION:
                return APPLICATION;
            default:
                throw new ScopeNotMatchedException(scope);
        }
    }
}
