package org.quickweb.session;

import java.util.Objects;

public class CP {
    private String column;
    private String param;

    private CP(String column, String param) {
        this.column = column;
        this.param = param;
    }

    public static CP of(String column, String param) {
        return new CP(column, param);
    }

    public String getColumn() {
        return column;
    }

    public String getParam() {
        return param;
    }

    public static String[] getColumns(CP[] cps) {
        Objects.requireNonNull(cps);

        String[] columns = new String[cps.length];
        int i = 0;
        for (CP cp : cps) {
            columns[i++] = cp.getColumn();
        }
        return columns;
    }

    public static String[] getParams(CP[] cps) {
        Objects.requireNonNull(cps);

        String[] paramNames = new String[cps.length];
        int i = 0;
        for (CP cp : cps) {
            paramNames[i++] = cp.getParam();
        }
        return paramNames;
    }
}
