package org.quickweb.exception;

import org.quickweb.session.QuickSession;

public class DefaultErrorHandler implements ErrorHandler {
    private static final DefaultErrorHandler INSTANCE = new DefaultErrorHandler();

    private DefaultErrorHandler() {
    }

    @Override
    public void onError(Exception e, QuickSession session) {
        e.printStackTrace();
    }

    public static DefaultErrorHandler getInstance() {
        return INSTANCE;
    }
}
