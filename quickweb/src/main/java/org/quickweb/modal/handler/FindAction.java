package org.quickweb.modal.handler;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public interface FindAction {
    void act(PreparedStatement stmt, ResultSet rs) throws SQLException;
}
