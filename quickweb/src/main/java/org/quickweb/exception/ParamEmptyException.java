package org.quickweb.exception;

import java.util.Objects;

public class ParamEmptyException extends RuntimeException {

    public ParamEmptyException() {
        this("param");
    }

    public ParamEmptyException(String param) {
        super(Objects.requireNonNull(param) + " can't be empty");
    }
}
