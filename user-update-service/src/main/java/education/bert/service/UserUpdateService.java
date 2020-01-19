package education.bert.service;

import education.bert.jdbc.JdbcHelper;
import education.bert.model.UserModel;

public class UserUpdateService {
    private String dbUrl;

    public void setDbUrl(String dbUrl) {
        this.dbUrl = dbUrl;
    }

    public UserModel saveUser(UserModel user) {
        UserModel result = new UserModel(user.getId(), user.getName());
        if (user.getId() == 0) {
            int id = JdbcHelper.executeUpdateWithId(
                    dbUrl,
                    "INSERT INTO users(name) VALUES (?);",
                    statement ->
                    {
                        statement.setString(1, user.getName());
                        return statement;
                    }
            );
            result.setId(id);
        } else if (0 ==
                JdbcHelper.executeUpdate(
                        dbUrl,
                        "UPDATE users SET name = ? WHERE id = ?;",
                        statement ->
                        {
                            statement.setString(1, user.getName());
                            statement.setInt(2, user.getId());
                            return statement;
                        }
                )
        ) {
            return null;
        }
        return result;
    }

    public boolean removeUser(int id) {
        return 0 != JdbcHelper.executeUpdate(
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
