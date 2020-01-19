package education.bert.service;

import education.bert.jdbc.JdbcHelper;
import education.bert.model.UserModel;

public class UserUpdateService {
    private String dbUrl;

    public void setDbUrl(String dbUrl) {
        this.dbUrl = dbUrl;
    }

    public int saveUser(UserModel user) {
        if (user.getId() == 0) {
            return JdbcHelper.executeUpdate(
                    dbUrl,
                    "INSERT INTO users(name) VALUES (?);",
                    statement ->
                    {
                        statement.setString(1, user.getName());
                        return statement;
                    }
            );
        } else {
            return JdbcHelper.executeUpdate(
                    dbUrl,
                    "UPDATE users SET name = ? WHERE id = ?;",
                    statement ->
                    {
                        statement.setString(1, user.getName());
                        statement.setInt(2, user.getId());
                        return statement;
                    }
            );
        }
    }

    public int removeUser(int id) {
        return JdbcHelper.executeUpdate(
                dbUrl,
                "DELETE FROM users WHERE id = ?;",
                statement ->
                {
                    statement.setInt(1, id);
                    return statement;
                }
        );
    }
}
