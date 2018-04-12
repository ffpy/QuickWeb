package org.quickweb.sample;

import org.quickweb.session.EditableParamScope;
import org.quickweb.session.ParamScope;

import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class App {
    public static void main(String[] args) {
        EditableParamScope editableParamScope = null;
        ParamScope scope = ParamScope.CONTEXT;

        EditableParamScope[] editableParamScopes = EditableParamScope.values();
        for (EditableParamScope ep : editableParamScopes) {
            if (Objects.equals(scope.name(), ep.name()))
                editableParamScope = ep;
        }
        System.out.println(editableParamScope);
        System.out.println(editableParamScope.getClass());
    }
}
