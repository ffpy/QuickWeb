package org.quickweb.modal;

import org.quickweb.session.param.CP;
import org.quickweb.session.QuickSession;

public interface QuickModal {

    String getTable();

    QuickSession getQuickSession();

    QuickModal select(String... columns);

    QuickModal selectFrom(String paramName);

    QuickModal where(String condition);

    QuickModal whereFrom(String paramName);

    QuickModal order(String... columns);

    QuickModal orderFrom(String paramName);

    QuickModal offset(int value);

    QuickModal offsetFrom(String paramName);

    QuickModal limit(int value);

    QuickModal limitFrom(String paramName);

    QuickSession insert(String... paramNames);

    QuickSession insert(CP... cps);

    QuickSession update(String... paramNames);

    QuickSession update(CP... cps);

    QuickSession delete();

    QuickSession findFirst(String paramName);

    QuickSession find(String paramName);

    QuickSession count(String paramName);

    QuickSession avg(String paramName, String column);

    QuickSession max(String paramName, String column, ResultType resultType);

    QuickSession min(String paramName, String column, ResultType resultType);

    QuickSession sum(String paramName, String column, ResultType resultType);
}
