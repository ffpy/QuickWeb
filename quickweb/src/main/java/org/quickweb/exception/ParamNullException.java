package org.quickweb.exception;

public class ParamNullException extends Exception {
    private String paramName;

    public ParamNullException(String paramName) {
        super(paramName + " can't be null");
        this.paramName = paramName;
    }

    public String getParamName() {
        return paramName;
    }
}
