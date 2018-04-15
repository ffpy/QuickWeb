package org.quickweb.error;

import org.quickweb.session.QuickSession;

/**
 * 错误处理
 */
public interface ErrorHandler {
    void onError(Exception e, QuickSession quickSession);
}
