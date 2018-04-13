package org.quickweb.modal;

import org.apache.commons.lang3.StringUtils;
import org.quickweb.utils.ExceptionUtils;

public class SqlParam {
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
            ExceptionUtils.throwMustMoreThanException("offset", -1);
        this.offset = value;
    }

    public int getLimit() {
        return limit;
    }

    public void setLimit(int value) {
        if (value < 0)
            ExceptionUtils.throwMustMoreThanException("limit", -1);
        this.limit = value;
    }

    private String paramJoin(String... ss) {
        return StringUtils.join(ss, ',');
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
