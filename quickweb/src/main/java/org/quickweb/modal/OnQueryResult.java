package org.quickweb.modal;

import org.quickweb.session.QuickSession;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public interface OnQueryResult {
    void onResult(ResultSet rs, PreparedStatement stmt, QuickSession quickSession) throws SQLException;
}
