package org.quickweb.session.scope;

import org.quickweb.utils.ExceptionUtils;
import org.quickweb.utils.RequireUtils;

import java.util.Objects;

public enum Scope implements IScope {
    CONTEXT,
    REQUEST,
    MODAL,
    SESSION,
    COOKIE,
    APPLICATION,
    ALL,
    ;

    public static Scope of(EditableScope scope) {
        RequireUtils.requireNotNull(scope);

        for (Scope ps : values()) {
            if (Objects.equals(ps.name(), scope.name()))
                return ps;
        }
        ExceptionUtils.throwScopeNotMatchedException(scope);
        return null;
    }
}
