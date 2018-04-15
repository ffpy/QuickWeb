package org.quickweb.session.param;

import org.quickweb.session.QuickSession;

public interface ParamGenerator {
    Object generate(QuickSession quickSession) throws Exception;
}
