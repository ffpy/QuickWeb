package org.quickweb.modal.handler;

import java.sql.PreparedStatement;
import java.sql.SQLException;

public interface Action {
    void act(PreparedStatement stmt) throws SQLException;
}
