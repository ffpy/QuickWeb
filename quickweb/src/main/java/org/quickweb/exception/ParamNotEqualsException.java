package org.quickweb.exception;

public class ParamNotEqualsException extends Exception {
    private String paramName;
    private Object actualValue;
    private Object expectedValue;

    public ParamNotEqualsException(String paramName, Object actualValue, Object expectedValue) {
        super(paramName + " is not expected value");
        this.paramName = paramName;
        this.actualValue = actualValue;
        this.expectedValue = expectedValue;
    }

    public String getParamName() {
        return paramName;
    }

    public Object getActualValue() {
        return actualValue;
    }

    public Object getExpectedValue() {
        return expectedValue;
    }
}
