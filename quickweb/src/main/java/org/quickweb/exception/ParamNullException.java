package org.quickweb.exception;

import java.util.Objects;

public class ParamNullException extends NullPointerException {

    public ParamNullException(String param) {
        super(Objects.requireNonNull(param) + " can't be null");
    }
}
