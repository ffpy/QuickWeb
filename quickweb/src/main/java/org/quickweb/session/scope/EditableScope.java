package org.quickweb.session.scope;

import org.quickweb.utils.ExceptionUtils;

import java.util.Objects;

public enum EditableScope implements IScope {
    CONTEXT,
    MODAL,
    SESSION,
    APPLICATION,
    ;

    public static EditableScope of(Scope scope) {
        for (EditableScope ps : values()) {
            if (Objects.equals(ps.name(), scope.name()))
                return ps;
        }
        throw ExceptionUtils.notEditableParamScope(scope);
    }
}
