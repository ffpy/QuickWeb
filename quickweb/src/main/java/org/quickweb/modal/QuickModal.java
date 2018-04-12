package org.quickweb.modal;

import org.apache.commons.lang3.StringUtils;
import org.quickweb.session.QuickSession;
import org.quickweb.template.TemplateExpr;
import org.quickweb.utils.ObjectUtils;

import java.util.Arrays;

public class QuickModal {
    private String table;
    private QuickSession quickSession;
    private String select;
    private String where;
    private String order;
    private int offset = -1;
    private int limit = -1;

    public QuickModal(String table, QuickSession quickSession) {
        ObjectUtils.requireNotNull(table, quickSession);

        this.table = table;
        this.quickSession = quickSession;
    }

    public String getTable() {
        return table;
    }

    public QuickSession getQuickSession() {
        return quickSession;
    }

    public QuickModal select(String... columns) {
        ObjectUtils.requireCollectionNotEmpty(columns);

        this.select = StringUtils.join(columns, ',');
        return this;
    }

    public QuickModal where(String condition) {
        ObjectUtils.requireNotNull(condition);

        this.where = condition;
        return this;
    }

    public QuickModal order(String... columns) {
        ObjectUtils.requireCollectionNotEmpty(columns);

        this.order = StringUtils.join(columns, ',');
        return this;
    }

    public QuickModal offset(int value) {
        if (value < 0)
            throw new RuntimeException("offset must be more than -1");

        this.offset = value;
        return this;
    }

    public QuickModal limit(int value) {
        if (value < 0)
            throw new RuntimeException("offset must be more than -1");

        this.limit = value;
        return this;
    }

    public QuickSession insert(String... params) {
        Handler.save(this, table, params);
        return quickSession;
    }

    public QuickSession update(String... params) {
        Handler.update(this, table, params, where);
        return quickSession;
    }

    public QuickSession delete() {
        return quickSession;
    }

    public QuickSession delete(String effectRowsParamName) {
        ObjectUtils.requireNotNull(effectRowsParamName);

        return quickSession;
    }

    public QuickSession findFirst() {
        return quickSession;
    }

    public QuickSession find(String paramName) {
        ObjectUtils.requireNotNull(paramName);

        return quickSession;
    }

    public QuickSession count(String paramName) {
        ObjectUtils.requireNotNull(paramName);

        return quickSession;
    }

    public QuickSession avg(String paramName) {
        ObjectUtils.requireNotNull(paramName);

        return quickSession;
    }

    public QuickSession max(String paramName, ResultType resultType) {
        ObjectUtils.requireNotNull(paramName, resultType);

        return quickSession;
    }

    public QuickSession min(String paramName, ResultType resultType) {
        ObjectUtils.requireNotNull(paramName, resultType);

        return quickSession;
    }

    public QuickSession sum(String paramName, ResultType resultType) {
        ObjectUtils.requireNotNull(paramName, resultType);

        return quickSession;
    }
}
