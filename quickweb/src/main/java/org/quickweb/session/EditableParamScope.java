package org.quickweb.session;

import org.quickweb.exception.ScopeNotMatchedException;
import org.quickweb.utils.ObjectUtils;

public enum EditableParamScope implements Scope {
    CONTEXT,
    MODAL,
    APPLICATION,
    ;

    @SuppressWarnings("Duplicates")
    public static EditableParamScope of(ParamScope scope) {
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
