package org.quickweb.utils;

import org.quickweb.session.QuickSession;

import java.sql.PreparedStatement;
import java.sql.SQLException;

public class StmtUtils {

    public static void setParams(
            QuickSession quickSession, PreparedStatement stmt, String[] params) throws SQLException {
        ObjectUtils.requireNotNull(quickSession, stmt, params);
        ObjectUtils.requireCollectionNotEmpty(params);

        for (int i = 0; i < params.length; i++) {
            stmt.setObject(i + 1, quickSession.getParam(params[i]));
        }
    }
}
