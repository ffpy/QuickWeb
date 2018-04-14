package org.quickweb.template;

import org.quickweb.utils.RequireUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MatcherHelper {
//    public static final String PATTERN = "\\$((\\w+):?([\\w_]+))";
    public static final String PATTERN = "\\$((\\w*):?([\\w_]*)([\\w_.]*))";
    private static final Pattern COMPILE = Pattern.compile(PATTERN);
    private final Matcher matcher;

    public MatcherHelper(String template) {
        RequireUtils.requireNotNull(template);
        this.matcher = COMPILE.matcher(template);
    }

    public boolean find() {
        return matcher.find();
    }

    public String getMatch() {
        return matcher.group(0);
    }

    public String getParam() {
        return matcher.group(1);
    }

    public String getScopeName() {
        return matcher.group(2);
    }

    public String getParamName() {
        return matcher.group(3);
    }
}
