package org.quickweb.session.scope;

import org.quickweb.exception.ScopeNotMatchedException;
import org.quickweb.utils.RequireUtils;

import java.util.Objects;

public enum ParamScope implements Scope {
    CONTEXT,
    MODAL,
    REQUEST,
    SESSION,
    COOKIE,
    APPLICATION,
    ALL,
    ;

    public static ParamScope of(EditableParamScope scope) {
        RequireUtils.requireNotNull(scope);

        for (ParamScope ps : values()) {
            if (Objects.equals(ps.name(), scope.name()))
                return ps;
        }
        throw new ScopeNotMatchedException(scope);
    }
}
