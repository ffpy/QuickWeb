package org.quickweb.modal;

import com.sun.istack.internal.NotNull;
import org.quickweb.session.QuickSession;

public class QuickModal {
    private String table;
    private QuickSession quickSession;

    public QuickModal(@NotNull String table, @NotNull QuickSession quickSession) {
        this.table = table;
        this.quickSession = quickSession;
    }

    public QuickModal select(@NotNull String... columns) {
        return this;
    }

    public QuickModal where(@NotNull String condition) {
        return this;
    }

    public QuickModal order(@NotNull String... columns) {
        return this;
    }

    public QuickModal offset(int value) {
        return this;
    }

    public QuickModal limit(int value) {
        return this;
    }

    public QuickSession insert(@NotNull String name, Object value) {
        return quickSession;
    }

    public QuickSession insert(@NotNull String effectColsParamName, @NotNull String name, Object value) {
        return quickSession;
    }

    public QuickSession insertFromParam(@NotNull String... params) {
        return quickSession;
    }

    public QuickSession insertFromParamWithEffectCols(
            @NotNull String effectColsParamName, @NotNull String... param) {
        return quickSession;
    }

    public QuickSession update(@NotNull String name, Object value) {
        return quickSession;
    }

    public QuickSession update(@NotNull String effectColsParamName, @NotNull String name, Object value) {
        return quickSession;
    }

    public QuickSession updateFromParam(@NotNull String... params) {
        return quickSession;
    }

    public QuickSession updateFromParamWithEffectCols(
            @NotNull String effectColsParamName, @NotNull String... params) {
        return quickSession;
    }

    public QuickSession delete() {
        return quickSession;
    }

    public QuickSession delete(@NotNull String effectColsParamName) {
        return quickSession;
    }

    public QuickSession find() {
        return quickSession;
    }

    public QuickSession find(@NotNull String paramName) {
        return quickSession;
    }

    public QuickSession count(@NotNull String paramName) {
        return quickSession;
    }

    public QuickSession avg(@NotNull String paramName) {
        return quickSession;
    }

    public QuickSession max(@NotNull String paramName, @NotNull ResultType resultType) {
        return quickSession;
    }

    public QuickSession min(@NotNull String paramName, @NotNull ResultType resultType) {
        return quickSession;
    }

    public QuickSession sum(@NotNull String paramName, @NotNull ResultType resultType) {
        return quickSession;
    }
}
