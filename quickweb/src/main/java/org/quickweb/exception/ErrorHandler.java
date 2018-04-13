package org.quickweb.exception;

import org.quickweb.session.QuickSession;

public interface ErrorHandler {
    void onError(Exception e, QuickSession quickSession);
}
