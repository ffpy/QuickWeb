package org.quickweb.modal;

import org.quickweb.session.QuickSession;

public interface QuickModal {

    String getTable();

    QuickSession getQuickSession();

    QuickModal select(String... columns);

    QuickModal where(String condition);

    QuickModal order(String... columns);

    QuickModal offset(int value);

    QuickModal limit(int value);

    QuickSession insert(String... params);

    QuickSession update(String... params);

    QuickSession delete();

    QuickSession findFirst(String paramName);

    QuickSession find(String paramName);

    QuickSession count(String paramName);

    QuickSession avg(String paramName, String column);

    QuickSession max(String paramName, String column, ResultType resultType);

    QuickSession min(String paramName, String column, ResultType resultType);

    QuickSession sum(String paramName, String column, ResultType resultType);
}
