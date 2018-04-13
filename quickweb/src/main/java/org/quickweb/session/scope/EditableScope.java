package org.quickweb.session.scope;

import org.quickweb.utils.ExceptionUtils;
import org.quickweb.utils.RequireUtils;

import java.util.Objects;

public enum EditableScope implements IScope {
    CONTEXT,
    MODAL,
    APPLICATION,
    ;

    public static EditableScope of(Scope scope) {
        RequireUtils.requireNotNull(scope);

        for (EditableScope ps : values()) {
            if (Objects.equals(ps.name(), scope.name()))
                return ps;
        }
        ExceptionUtils.throwNotEditableParamScope(scope);
        return null;
    }
}
