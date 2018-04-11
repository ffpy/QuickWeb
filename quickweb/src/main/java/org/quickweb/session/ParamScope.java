package org.quickweb.session;

public enum ParamScope {
    CONTEXT,
    MODAL,
    REQUEST,
    SESSION,
    COOKIE,
    APPLICATION,
    ;

    public static ParamScope of(EditableParamScope scope) {
        switch (scope) {
            case CONTEXT:
                return CONTEXT;
            case MODAL:
                return MODAL;
            case APPLICATION:
                return APPLICATION;
        }
        return null;
    }
}
