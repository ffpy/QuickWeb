package org.quickweb.session.action;

import org.quickweb.session.QuickSession;

public interface RequireEqualsAction {
    void act(String paramName, Object actualValue, Object expectedValue,
             QuickSession quickSession) throws Exception;
}
