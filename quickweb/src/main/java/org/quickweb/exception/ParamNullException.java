package org.quickweb.exception;

import java.util.Objects;

public class ParamNullException extends NullPointerException {

    public ParamNullException(String paramName) {
        super(Objects.requireNonNull(paramName) + " can't be null");
    }
}
