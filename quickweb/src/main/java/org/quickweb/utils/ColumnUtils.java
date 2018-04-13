package org.quickweb.utils;

import org.quickweb.session.param.ParamHelper;

public class ColumnUtils {

    public static String[] getColumns(String[] columnAndParams) {
        RequireUtils.requireArrayNotNull(columnAndParams);
        String[] columns = new String[columnAndParams.length];
        ParamHelper helper = new ParamHelper();
        int i = 0;
        for (String cp : columnAndParams) {
            helper.setParam(cp);
            columns[i++] = helper.getParamName();
        }
        return columns;
    }
}
