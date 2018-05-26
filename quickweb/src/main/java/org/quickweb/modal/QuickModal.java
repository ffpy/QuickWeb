package org.quickweb.modal;

import org.quickweb.modal.param.ResultType;
import org.quickweb.modal.param.SqlParam;
import org.quickweb.session.param.CP;
import org.quickweb.session.QuickSession;

public interface QuickModal {
    // update_count参数名
    String UPDATE_COUNT = "m:quick_modal_update_count";
    // 主键参数名
    String GENERATED_KEY = "m:quick_modal_generated_key";

    void initProxy(QuickModal quickModalProxy);

    SqlParam getSqlParam();

    QuickSession getQuickSession();

    QuickModal select(String... columns);

    QuickModal selectFrom(String param);

    QuickModal where(String condition);

    QuickModal whereFrom(String param);

    QuickModal order(String... columns);

    QuickModal orderFrom(String param);

    QuickModal offset(int value);

    QuickModal offsetFrom(String param);

    QuickModal limit(int value);

    QuickModal limitFrom(String param);

    QuickSession insert(String... columnAndParamNames);

    QuickSession insert(CP... cps);

    QuickSession update(String... columnAndParamNames);

    QuickSession update(CP... cps);

    QuickSession delete();

    QuickSession findFirst(String param);

    QuickSession find(String param);

    QuickSession find(OnQueryResult onQueryResult);

    QuickSession count(String param);

    QuickSession avg(String param, String column);

    QuickSession max(String param, String column, ResultType resultType);

    QuickSession min(String param, String column, ResultType resultType);

    QuickSession sum(String param, String column, ResultType resultType);
}
