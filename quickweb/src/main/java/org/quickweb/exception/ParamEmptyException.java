package org.quickweb.exception;

public class ParamEmptyException extends RuntimeException {

    public ParamEmptyException() {
    }

    public ParamEmptyException(String paramName) {
        super(paramName + " can't be empty");
    }
}
