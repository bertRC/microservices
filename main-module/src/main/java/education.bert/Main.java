package education.bert;

import education.bert.jdbc.JdbcHelper;
import education.bert.model.UserModel;

import java.util.List;

public class Main {
    public static void main(String[] args) {
        final MyApplicationSmart app = new MyApplicationSmart();

        app.setup();
        app.userUpdateService.saveUser(new UserModel(0, "Vasya"));
        app.userUpdateService.saveUser(new UserModel(0, "Petya"));
        app.userUpdateService.saveUser(new UserModel(0, "Ivan"));
        app.userUpdateService.saveUser(new UserModel(0, "Masha"));
        app.userUpdateService.saveUser(new UserModel(0, "Sasha"));
        app.userUpdateService.saveUser(new UserModel(0, "Dasha"));
        app.userUpdateService.saveUser(new UserModel(0, "Bert"));
        app.userUpdateService.saveUser(new UserModel(0, "Kolya"));
        app.userUpdateService.saveUser(new UserModel(0, "Styopa"));
        app.userUpdateService.saveUser(new UserModel(0, "Alesha"));

        String name = "ash";
        List<String> results = JdbcHelper.executeQueryForList(
                PostgresConfig.url,
                "EXPLAIN ANALYZE SELECT id, name FROM users WHERE LOWER(name) LIKE ?;",
                statement ->
                {
                    statement.setString(1, "%" + name.toLowerCase() + "%");
                    return statement;
                },
                resultSet -> resultSet.getString(1)
        );

        System.out.println(results);
    }
}
