package org.quickweb.exception;

import org.quickweb.session.scope.Scope;

public class ScopeNotMatchedException extends RuntimeException {

    public ScopeNotMatchedException(Scope scope) {
        super("scope not matched, the value is " + scope);
    }
}
