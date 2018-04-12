package org.quickweb.modal;

import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;

public enum ResultType {
    BYTE(Byte.class),
    SHORT(Short.class),
    INT(Integer.class),
    LONG(Long.class),
    FLOAT(Float.class),
    DOUBLE(Double.class),
    DATE(Date.class),
    TIME(Time.class),
    DATETIME(Timestamp.class),
    STRING(String.class),
    ;

    private Class<?> type;

    ResultType(Class<?> type) {
        this.type = type;
    }

    public Class<?> getType() {
        return type;
    }
}
