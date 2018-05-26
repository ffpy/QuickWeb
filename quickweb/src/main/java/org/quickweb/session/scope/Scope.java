package org.quickweb.session.scope;

import org.quickweb.utils.ExceptionUtils;

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
        for (Scope ps : values()) {
            if (Objects.equals(ps.name(), scope.name()))
                return ps;
        }
        ExceptionUtils.nonsupportScope(scope);
        return null;
    }
}
