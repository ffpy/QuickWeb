package org.quickweb.session.action;

import org.quickweb.session.QuickSession;

public interface RequireEmptyAction {
    void act(String paramName, QuickSession quickSession) throws Exception;
}
