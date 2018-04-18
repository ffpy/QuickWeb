package org.quickweb.template;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MatcherHelper {
    public static final String PATTERN = "\\$(((\\w*):)?([\\w_]*[\\w_\\.\\[\\]]*))";
    private static final Pattern COMPILE = Pattern.compile(PATTERN);
    private final Matcher matcher;

    public MatcherHelper(String template) {
        template = template.replace("$$", "");
        this.matcher = COMPILE.matcher(template);
    }

    public boolean find() {
        return matcher.find();
    }

    public String getMatch() {
        String s = matcher.group(0);
        return s == null ? "" : s;
    }

    public String getParam() {
        String s = matcher.group(1);
        return s == null ? "" : s;
    }

    public String getScopeName() {
        String s = matcher.group(3);
        return s == null ? "" : s;
    }
}
