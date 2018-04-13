package org.quickweb.utils;

import org.quickweb.session.param.ParamHelper;

public class ColumnUtils {

    public static String[] getColumns(String[] columnAndParamNames) {
        RequireUtils.requireArrayNotNull(columnAndParamNames);
        String[] columns = new String[columnAndParamNames.length];
        ParamHelper helper = new ParamHelper();
        int i = 0;
        for (String cp : columnAndParamNames) {
            helper.setParam(cp);
            columns[i++] = helper.getParamName();
        }
        return columns;
    }
}
