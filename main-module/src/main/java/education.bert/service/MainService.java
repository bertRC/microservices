package education.bert.service;

import education.bert.jdbc.JdbcHelper;

public class MainService {
    private String dbUrl;

    public void setDbUrl(String dbUrl) {
        this.dbUrl = dbUrl;
    }

    public void createTables() {
        JdbcHelper.executeUpdate(dbUrl, "DROP TABLE IF EXISTS users;");
        JdbcHelper.executeUpdate(dbUrl, "CREATE TABLE users (id SERIAL PRIMARY KEY, name TEXT NOT NULL);");
    }
}
