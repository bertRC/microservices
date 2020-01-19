package education.bert.service;

import education.bert.jdbc.JdbcHelper;
import education.bert.model.UserModel;

public class UserSearchService {
    private String dbUrl;

    public void setDbUrl(String dbUrl) {
        this.dbUrl = dbUrl;
    }

    public UserModel getUser(int id) {
        return JdbcHelper.executeQueryForObject(
                dbUrl,
                "SELECT id, name FROM users WHERE id = ?;",
                statement ->
                {
                    statement.setInt(1, id);
                    return statement;
                },
                resultSet -> new UserModel(
                        resultSet.getInt("id"),
                        resultSet.getString("name")
                )
        ).orElse(null);
    }
}
