package org.quickweb.modal;

import org.quickweb.session.QuickSession;
import org.quickweb.utils.RequireUtils;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

public class StmtHelper {
    private QuickSession quickSession;
    private PreparedStatement stmt;
    private int index = 1;

    public StmtHelper(QuickSession quickSession, PreparedStatement stmt) {
        RequireUtils.requireNotNull(quickSession, stmt);
        this.quickSession = quickSession;
        this.stmt = stmt;
    }

    public void setParams(String[] paramNames) throws SQLException {
        RequireUtils.requireArrayNotNull(paramNames);

        for (String paramName : paramNames) {
            stmt.setObject(index++, quickSession.getParam(paramName));
        }
    }

    public void setParams(List<Object> values) throws SQLException {
        RequireUtils.requireNotNull(values);

        for (Object value : values) {
            stmt.setObject(index++, value);
        }
    }
}
