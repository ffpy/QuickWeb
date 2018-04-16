package org.quickweb.modal.param;

import org.quickweb.session.QuickSession;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

public class StmtHelper {
    private QuickSession quickSession;
    private PreparedStatement stmt;
    private int index = 1;

    public StmtHelper(QuickSession quickSession, PreparedStatement stmt) {
        this.quickSession = quickSession;
        this.stmt = stmt;
    }

    public StmtHelper setParams(String[] paramNames) throws SQLException {
        for (String paramName : paramNames) {
            stmt.setObject(index++, quickSession.getParam(paramName));
        }
        return this;
    }

    public StmtHelper setParams(List<Object> values) throws SQLException {
        for (Object value : values) {
            stmt.setObject(index++, value);
        }
        return this;
    }
}
