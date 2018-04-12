package org.quickweb.modal.handler;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public interface Action {
    void act(Connection conn, PreparedStatement stmt) throws SQLException;
}
