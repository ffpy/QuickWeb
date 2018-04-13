package org.quickweb;

public class QuickWebConfig {
    private Session session;
    private DB db;
    private View view;

    public Session getSession() {
        return session;
    }

    public void setSession(Session session) {
        this.session = session;
    }

    public DB getDb() {
        return db;
    }

    public void setDb(DB db) {
        this.db = db;
    }

    public View getView() {
        return view;
    }

    public void setView(View view) {
        this.view = view;
    }

    @Override
    public String toString() {
        return "QuickWebConfig{" +
                "session=" + session +
                ", db=" + db +
                ", view=" + view +
                '}';
    }

    public static class Session {
        private String charset;

        public String getCharset() {
            return charset;
        }

        public void setCharset(String charset) {
            this.charset = charset;
        }

        @Override
        public String toString() {
            return "Session{" +
                    "charset='" + charset + '\'' +
                    '}';
        }
    }

    public static class DB {
        private String driver;
        private String url;
        private String username;
        private String password;
        private boolean showSql;

        public String getDriver() {
            return driver;
        }

        public void setDriver(String driver) {
            this.driver = driver;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }

        public boolean isShowSql() {
            return showSql;
        }

        public void setShowSql(boolean showSql) {
            this.showSql = showSql;
        }

        @Override
        public String toString() {
            return "DB{" +
                    "driver='" + driver + '\'' +
                    ", url='" + url + '\'' +
                    ", username='" + username + '\'' +
                    ", password='" + password + '\'' +
                    ", showSql=" + showSql +
                    '}';
        }
    }

    public static class View {
        private String prefix;
        private String suffix;

        public String getPrefix() {
            return prefix;
        }

        public void setPrefix(String prefix) {
            this.prefix = prefix;
        }

        public String getSuffix() {
            return suffix;
        }

        public void setSuffix(String suffix) {
            this.suffix = suffix;
        }

        @Override
        public String toString() {
            return "View{" +
                    "prefix='" + prefix + '\'' +
                    ", suffix='" + suffix + '\'' +
                    '}';
        }
    }
}
