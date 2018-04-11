package org.quickweb.exception;

import java.util.Objects;

public class ParamEmptyException extends RuntimeException {

    public ParamEmptyException() {
    }

    public ParamEmptyException(String paramName) {
        super(Objects.requireNonNull(paramName) + " can't be empty");
    }
}
