package education.bert;

import education.bert.jdbc.JdbcHelper;
import education.bert.service.PostSearchService;
import education.bert.service.PostUpdateService;
import education.bert.service.UserSearchService;
import education.bert.service.UserUpdateService;

public class MyApplication {
    private final String dbUrl = PostgresConfig.url;

    public UserUpdateService userUpdateService = new UserUpdateService();
    public UserSearchService userSearchService = new UserSearchService();
    public PostUpdateService postUpdateService = new PostUpdateService();
    public PostSearchService postSearchService = new PostSearchService();

    public void setup() {
        userUpdateService.setDbUrl(dbUrl);
        userSearchService.setDbUrl(dbUrl);
        postUpdateService.setDbUrl(dbUrl);
        postSearchService.setDbUrl(dbUrl);

        JdbcHelper.executeUpdate(dbUrl, "DROP TABLE IF EXISTS users;");
        JdbcHelper.executeUpdate(dbUrl, "DROP TABLE IF EXISTS posts;");
        JdbcHelper.executeUpdate(dbUrl, "CREATE TABLE users (id SERIAL PRIMARY KEY, name TEXT NOT NULL);");
        JdbcHelper.executeUpdate(dbUrl, "CREATE TABLE posts (id SERIAL PRIMARY KEY, postName TEXT NOT NULL, creatorId INTEGER);");
    }

    public void dropTables() {
        JdbcHelper.executeUpdate(dbUrl, "DROP TABLE IF EXISTS users;");
        JdbcHelper.executeUpdate(dbUrl, "DROP TABLE IF EXISTS posts;");
    }
}
