package org.quickweb.modal;

import org.quickweb.session.param.CP;
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

    QuickSession insert(CP... cps);

    QuickSession update(String... params);

    QuickSession update(CP... cps);

    QuickSession delete();

    QuickSession findFirst(String param);

    QuickSession find(String param);

    QuickSession count(String param);

    QuickSession avg(String param, String column);

    QuickSession max(String param, String column, ResultType resultType);

    QuickSession min(String param, String column, ResultType resultType);

    QuickSession sum(String param, String column, ResultType resultType);
}
