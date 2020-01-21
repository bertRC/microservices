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

    public void setup() {
        userUpdateService.setDbUrl(dbUrl);
        userUpdateSmartService.setDbUrl(dbUrl);
        userSearchService.setDbUrl(dbUrl);
        userSearchSmartService.setDbUrl(dbUrl);

        userUpdateSmartService.setVcService(vcService);
        userSearchSmartService.setVcService(vcService);

        JdbcHelper.executeUpdate(dbUrl, "DROP TABLE IF EXISTS users;");
        JdbcHelper.executeUpdate(dbUrl, "CREATE TABLE users (id SERIAL PRIMARY KEY, name TEXT NOT NULL);");
    }

    public void dropTables() {
        JdbcHelper.executeUpdate(dbUrl, "DROP TABLE IF EXISTS users;");
    }

    public void dropTablesSmart() {
        dropTables();
        vcService.removeTable("users");
    }
}
