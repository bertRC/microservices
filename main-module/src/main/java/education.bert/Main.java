package education.bert;

import education.bert.jdbc.JdbcHelper;
import education.bert.model.UserModel;
import education.bert.service.UserSearchService;
import education.bert.service.UserUpdateService;

import java.util.List;

public class Main {
    public static void main(String[] args) {
        final String dbUrl = PostgresConfig.url;
        final UserUpdateService userUpdateService = new UserUpdateService();
        final UserSearchService userSearchService = new UserSearchService();

        userUpdateService.setDbUrl(dbUrl);
        userSearchService.setDbUrl(dbUrl);

        JdbcHelper.executeUpdate(dbUrl, "DROP TABLE IF EXISTS users;");
        JdbcHelper.executeUpdate(dbUrl, "CREATE TABLE users (id SERIAL PRIMARY KEY, name TEXT NOT NULL);");

        userUpdateService.saveUser(new UserModel(0, "Vasya"));

//        UserModel user = userSearchService.getUser(1);
//        System.out.println(user);

        int id = 1;
        final List<String> list = JdbcHelper.executeQueryForList(
                dbUrl,
                "EXPLAIN ANALYZE SELECT id, name FROM users WHERE id = ?;",
                statement ->
                {
                    statement.setInt(1, id);
                    return statement;
                },
                resultSet ->
                {
                    return resultSet.getString(1);
                }
        );

        System.out.println(list.size());
        System.out.println(list);
    }
}
