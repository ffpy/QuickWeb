package org.quickweb.exception;

public class ParamNotEqualsException extends RuntimeException {

    public ParamNotEqualsException(String name, Object expectedValue, Object actualValue) {
        super("the expected value of " + name + " is " + expectedValue +
                ", and the actual value is " + actualValue);
    }
}
