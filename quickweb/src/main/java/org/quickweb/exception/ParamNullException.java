package org.quickweb.exception;

/**
 * 参数为Null异常
 */
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
