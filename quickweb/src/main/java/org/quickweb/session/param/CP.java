package org.quickweb.session.param;

import java.util.Objects;

public class CP {
    private String column;
    private String paramName;

    private CP(String column, String paramName) {
        this.column = column;
        this.paramName = paramName;
    }

    public static CP of(String column, String paramName) {
        return new CP(column, paramName);
    }

    public String getColumn() {
        return column;
    }

    public String getParamName() {
        return paramName;
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

        String[] paramNames = new String[cps.length];
        int i = 0;
        for (CP cp : cps) {
            paramNames[i++] = cp.getParamName();
        }
        return paramNames;
    }

    @Override
    public String toString() {
        return "CP{" +
                "column='" + column + '\'' +
                ", paramName='" + paramName + '\'' +
                '}';
    }
}
