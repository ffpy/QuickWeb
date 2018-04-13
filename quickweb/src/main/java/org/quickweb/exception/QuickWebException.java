package org.quickweb.exception;

public class QuickWebException extends RuntimeException {

    public QuickWebException() {
    }

    public QuickWebException(String message) {
        super(message);
    }

    public QuickWebException(String message, Throwable cause) {
        super(message, cause);
    }

    public QuickWebException(Throwable cause) {
        super(cause);
    }
}
