package org.quickweb.exception;

public class ParamNullException extends NullPointerException {

    public ParamNullException() {
        super();
    }

    public ParamNullException(String paramName) {
        super(paramName + " can't be null.");
    }
}
