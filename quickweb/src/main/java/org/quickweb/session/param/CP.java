package org.quickweb.session.param;

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

    public static CP[] of(String[] columnAndParamNames) {
        CP[] cps = new CP[columnAndParamNames.length];
        int i = 0;
        for (String cp : columnAndParamNames) {
            cps[i++] = CP.of(cp, cp);
        }
        return cps;
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

    public static String[] getParamNames(CP[] cps) {
        Objects.requireNonNull(cps);

        String[] params = new String[cps.length];
        int i = 0;
        for (CP cp : cps) {
            params[i++] = cp.getParam();
        }
        return params;
    }

    @Override
    public String toString() {
        return "CP{" +
                "column='" + column + '\'' +
                ", param='" + param + '\'' +
                '}';
    }
}
