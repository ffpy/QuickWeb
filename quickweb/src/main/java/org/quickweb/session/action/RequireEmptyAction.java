package org.quickweb.session.action;

import org.quickweb.session.QuickSession;

public interface RequireEmptyAction {
    void act(String param, QuickSession quickSession) throws Exception;
}
