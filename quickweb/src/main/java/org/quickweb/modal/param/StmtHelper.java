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

    public StmtHelper setParams(String[] params) throws SQLException {
        for (String p : params) {
            stmt.setObject(index++, quickSession.getParam(p));
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
