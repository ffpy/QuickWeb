package org.quickweb.error;

import org.quickweb.session.QuickSession;

/**
 * 默认错误处理
 */
public class DefaultErrorHandler implements ErrorHandler {
    private static final DefaultErrorHandler INSTANCE = new DefaultErrorHandler();

    private DefaultErrorHandler() {
    }

    @Override
    public void onError(Exception e, QuickSession quickSession) {
        quickSession.end();
        e.printStackTrace();
    }

    public static DefaultErrorHandler getInstance() {
        return INSTANCE;
    }
}
