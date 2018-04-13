package org.quickweb.session.param;

import org.quickweb.session.QuickSession;

import java.util.function.Function;

public interface ParamGenerator extends Function<QuickSession, Object> {
}
