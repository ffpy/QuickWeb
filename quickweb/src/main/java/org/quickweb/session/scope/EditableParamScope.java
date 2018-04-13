package org.quickweb.session.scope;

import org.quickweb.exception.ScopeNotMatchedException;
import org.quickweb.utils.RequireUtils;

import java.util.Objects;

public enum EditableParamScope implements Scope {
    CONTEXT,
    MODAL,
    APPLICATION,
    ;

    public static EditableParamScope of(ParamScope scope) {
        RequireUtils.requireNotNull(scope);

        for (EditableParamScope ps : values()) {
            if (Objects.equals(ps.name(), scope.name()))
                return ps;
        }
        throw new ScopeNotMatchedException(scope);
    }
}
