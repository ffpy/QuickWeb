package org.quickweb.exception;

import java.util.Objects;

public class ParamNotEqualsException extends RuntimeException {

    public ParamNotEqualsException(String name, Object expectedValue, Object actualValue) {
        super("the expected value of " + Objects.requireNonNull(name) +
                " is " + expectedValue + ", and the actual value is " + actualValue);
    }
}
