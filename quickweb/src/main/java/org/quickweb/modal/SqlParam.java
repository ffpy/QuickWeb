package org.quickweb.modal;

import org.apache.commons.lang3.StringUtils;

public class SqlParam implements Cloneable {
    private String table = "";
    private String select = "";
    private String where = "";
    private String order = "";
    private int offset = -1;
    private int limit = -1;

    public String getTable() {
        return table;
    }

    public void setTable(String table) {
        this.table = table;
    }

    public String getSelect() {
        return select;
    }

    public void setSelect(String... columns) {
        this.select = paramJoin(columns);
    }

    public String getWhere() {
        return where;
    }

    public void setWhere(String where) {
        this.where = where;
    }

    public String getOrder() {
        return order;
    }

    public void setOrder(String... columns) {
        this.order = paramJoin(columns);
    }

    public int getOffset() {
        return offset;
    }

    public void setOffset(int value) {
        if (value < 0)
            throw new RuntimeException("offset must be more than -1");
        this.offset = value;
    }

    public int getLimit() {
        return limit;
    }

    public void setLimit(int value) {
        if (value < 0)
            throw new RuntimeException("limit must be more than -1");
        this.limit = value;
    }

    private String paramJoin(String... ss) {
        return StringUtils.join(ss, ',');
    }

    @Override
    public SqlParam clone() {
        try {
            return (SqlParam) super.clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public String toString() {
        return "SqlParam{" +
                "table='" + table + '\'' +
                ", select='" + select + '\'' +
                ", where='" + where + '\'' +
                ", order='" + order + '\'' +
                ", offset=" + offset +
                ", limit=" + limit +
                '}';
    }
}
