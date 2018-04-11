package org.quickweb.session;

import org.quickweb.exception.ScopeNotMatchedException;
import org.quickweb.utils.ObjectUtils;

public enum ParamScope implements Scope {
    CONTEXT,
    MODAL,
    REQUEST,
    SESSION,
    COOKIE,
    APPLICATION,
    ;

    @SuppressWarnings("Duplicates")
    public static ParamScope of(EditableParamScope scope) {
        ObjectUtils.requireNonNull(scope);

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
