package org.quickweb.session;

public enum EditableParamScope {
    CONTEXT,
    MODAL,
    APPLICATION,
    ;

    public static EditableParamScope of(ParamScope scope) {
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
