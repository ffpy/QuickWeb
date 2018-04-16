package org.quickweb.modal.param;

import org.quickweb.utils.ExceptionUtils;

import java.util.Arrays;

public class SqlParam {
    private String[] tables;
    private String[] columns;
    private String condition;
    private String[] orders;
    private int offset = -1;
    private int limit = -1;

    public String[] getTables() {
        return tables;
    }

    public void setTables(String[] tables) {
        this.tables = tables;
    }

    public String[] getColumns() {
        return columns;
    }

    public void setColumns(String[] columns) {
        this.columns = columns;
    }

    public String getCondition() {
        return condition;
    }

    public void setCondition(String condition) {
        this.condition = condition;
    }

    public String[] getOrders() {
        return orders;
    }

    public void setOrders(String[] orders) {
        this.orders = orders;
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

    @Override
    public String toString() {
        return "SqlParam{" +
                "tables=" + Arrays.toString(tables) +
                ", columns=" + Arrays.toString(columns) +
                ", condition='" + condition + '\'' +
                ", orders=" + Arrays.toString(orders) +
                ", offset=" + offset +
                ", limit=" + limit +
                '}';
    }
}
