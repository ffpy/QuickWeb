package org.quickweb.session.action;

import org.quickweb.session.QuickSession;

import java.sql.PreparedStatement;
import java.sql.SQLException;

public interface ExecSQLAction {
    void exec(PreparedStatement stmt, QuickSession quickSession) throws SQLException;
}
