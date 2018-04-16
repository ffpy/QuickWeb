package org.quickweb.modal.bulider;

import org.quickweb.modal.param.SqlParamHelper;

public interface SqlBuilder {

    String insert(SqlParamHelper helper);

    String update(SqlParamHelper helper);

    String delete(SqlParamHelper helper);

    String find(SqlParamHelper helper);
}
