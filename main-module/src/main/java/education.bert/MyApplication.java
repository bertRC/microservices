package education.bert;

import education.bert.jdbc.JdbcHelper;
import education.bert.service.*;

public class MyApplication {
    private final String dbUrl = PostgresConfig.url;

    public VCService vcService = new VCService();

    public UserUpdateService userUpdateService = new UserUpdateService();
    public UserUpdateSmartService userUpdateSmartService = new UserUpdateSmartService();
    public UserSearchService userSearchService = new UserSearchService();
    public UserSearchSmartService userSearchSmartService = new UserSearchSmartService();
    public PostUpdateService postUpdateService = new PostUpdateService();
    public PostUpdateSmartService postUpdateSmartService = new PostUpdateSmartService();
    public PostSearchService postSearchService = new PostSearchService();
    public PostSearchSmartService postSearchSmartService = new PostSearchSmartService();

    public void setup() {
        userUpdateService.setDbUrl(dbUrl);
        userUpdateSmartService.setDbUrl(dbUrl);
        userSearchService.setDbUrl(dbUrl);
        userSearchSmartService.setDbUrl(dbUrl);
        postUpdateService.setDbUrl(dbUrl);
        postUpdateSmartService.setDbUrl(dbUrl);
        postSearchService.setDbUrl(dbUrl);
        postSearchSmartService.setDbUrl(dbUrl);

        userUpdateSmartService.setVcService(vcService);
        userSearchSmartService.setVcService(vcService);
        postUpdateSmartService.setVcService(vcService);
        postSearchSmartService.setVcService(vcService);

        JdbcHelper.executeUpdate(dbUrl, "DROP TABLE IF EXISTS users;");
        JdbcHelper.executeUpdate(dbUrl, "DROP TABLE IF EXISTS posts;");
        JdbcHelper.executeUpdate(dbUrl, "CREATE TABLE users (id SERIAL PRIMARY KEY, name TEXT NOT NULL);");
        JdbcHelper.executeUpdate(dbUrl, "CREATE TABLE posts (id SERIAL PRIMARY KEY, postName TEXT NOT NULL, creatorId INTEGER);");
    }

    public void dropTables() {
        JdbcHelper.executeUpdate(dbUrl, "DROP TABLE IF EXISTS users;");
        JdbcHelper.executeUpdate(dbUrl, "DROP TABLE IF EXISTS posts;");
    }

    public void dropTablesSmart() {
        dropTables();
        vcService.removeTable("users");
    }
}
