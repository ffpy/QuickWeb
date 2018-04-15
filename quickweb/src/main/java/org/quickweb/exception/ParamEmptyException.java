package org.quickweb.exception;

/**
 * 参数为空异常
 */
public class ParamEmptyException extends Exception {
    private String paramName;

    public ParamEmptyException(String paramName) {
        super(paramName + " can't be empty");
        this.paramName = paramName;
    }

    public String getParamName() {
        return paramName;
    }
}
