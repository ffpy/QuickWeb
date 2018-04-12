package org.quickweb.exception;

import java.util.Objects;

public class ParamNotEqualsException extends RuntimeException {

    public ParamNotEqualsException(String param, Object expectedValue, Object actualValue) {
        super("the expected value of " + Objects.requireNonNull(param) +
                " is " + expectedValue + ", and the actual value is " + actualValue);
    }
}
