package org.quickweb.modal;

import com.sun.istack.internal.Nullable;
import org.quickweb.session.QuickSession;
import org.quickweb.utils.ObjectUtils;

public class QuickModal {
    private String table;
    private QuickSession quickSession;

    public QuickModal(String table, QuickSession quickSession) {
        ObjectUtils.requireNonNull(table, quickSession);

        this.table = table;
        this.quickSession = quickSession;
    }

    public QuickModal select(String... columns) {
        ObjectUtils.requireNonNull(columns);

        return this;
    }

    public QuickModal where(String condition) {
        ObjectUtils.requireNonNull(condition);

        return this;
    }

    public QuickModal order(String... columns) {
        ObjectUtils.requireNonNull(columns);

        return this;
    }

    public QuickModal offset(int value) {
        return this;
    }

    public QuickModal limit(int value) {
        return this;
    }

    public QuickSession insert(String name, @Nullable Object value) {
        ObjectUtils.requireNonNull(name);

        return quickSession;
    }

    public QuickSession insert(String effectColsParamName, String name, @Nullable Object value) {
        ObjectUtils.requireNonNull(effectColsParamName, name);

        return quickSession;
    }

    public QuickSession insertFromParam(String... params) {
        ObjectUtils.requireNonNull(params);

        return quickSession;
    }

    public QuickSession insertFromParamWithEffectCols(
            String effectColsParamName, String... param) {
        ObjectUtils.requireNonNull(effectColsParamName, param);

        return quickSession;
    }

    public QuickSession update(String name, @Nullable Object value) {
        ObjectUtils.requireNonNull(name);

        return quickSession;
    }

    public QuickSession update(String effectColsParamName, String name, @Nullable Object value) {
        ObjectUtils.requireNonNull(effectColsParamName, name);

        return quickSession;
    }

    public QuickSession updateFromParam(String... params) {
        ObjectUtils.requireNonNull(params);

        return quickSession;
    }

    public QuickSession updateFromParamWithEffectCols(
            String effectColsParamName, String... params) {
        ObjectUtils.requireNonNull(effectColsParamName, params);

        return quickSession;
    }

    public QuickSession delete() {
        return quickSession;
    }

    public QuickSession delete(String effectColsParamName) {
        ObjectUtils.requireNonNull(effectColsParamName);

        return quickSession;
    }

    public QuickSession find() {
        return quickSession;
    }

    public QuickSession find(String paramName) {
        ObjectUtils.requireNonNull(paramName);

        return quickSession;
    }

    public QuickSession count(String paramName) {
        ObjectUtils.requireNonNull(paramName);

        return quickSession;
    }

    public QuickSession avg(String paramName) {
        ObjectUtils.requireNonNull(paramName);

        return quickSession;
    }

    public QuickSession max(String paramName, ResultType resultType) {
        ObjectUtils.requireNonNull(paramName, resultType);

        return quickSession;
    }

    public QuickSession min(String paramName, ResultType resultType) {
        ObjectUtils.requireNonNull(paramName, resultType);

        return quickSession;
    }

    public QuickSession sum(String paramName, ResultType resultType) {
        ObjectUtils.requireNonNull(paramName, resultType);

        return quickSession;
    }
}
