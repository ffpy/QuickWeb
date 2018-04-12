package org.quickweb.modal;

import org.quickweb.session.QuickSession;
import org.quickweb.utils.ObjectUtils;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

public class StmtHelper {
    private QuickSession quickSession;
    private PreparedStatement stmt;
    private int index = 1;

    public StmtHelper(QuickSession quickSession, PreparedStatement stmt) {
        ObjectUtils.requireNotNull(quickSession, stmt);

        this.quickSession = quickSession;
        this.stmt = stmt;
    }

    public StmtHelper(PreparedStatement stmt) {
        ObjectUtils.requireNotNull(stmt);

        this.stmt = stmt;
    }

    public void setParams(String[] params) throws SQLException {
        ObjectUtils.requireNotNull((Object) params);

        for (String param : params) {
            stmt.setObject(index++, quickSession.getParam(param));
        }
    }

    public void setParams(List<Object> values) throws SQLException {
        ObjectUtils.requireNotNull(values);

        for (Object value : values) {
            stmt.setObject(index++, value);
        }
    }
}