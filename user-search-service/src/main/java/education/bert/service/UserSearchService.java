package education.bert.service;

import education.bert.jdbc.JdbcHelper;
import education.bert.model.UserModel;

import java.util.List;

public class UserSearchService {
    private String dbUrl;

    private final int minTextLengthForSearch = 3;

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

    public List<UserModel> getAll() {
        return JdbcHelper.executeQueryForList(
                dbUrl,
                "SELECT id, name FROM users;",
                resultSet -> new UserModel(
                        resultSet.getInt("id"),
                        resultSet.getString("name")
                )
        );
    }

    public List<UserModel> searchByName(String name) {
        if (name.length() < minTextLengthForSearch) {
            throw new IllegalArgumentException("Text must contain at least " + minTextLengthForSearch + " characters");
        }
        return JdbcHelper.executeQueryForList(
                dbUrl,
                "SELECT id, name FROM users WHERE LOWER(name) LIKE ?;",
                statement ->
                {
                    statement.setString(1, "%" + name.toLowerCase() + "%");
                    return statement;
                },
                resultSet -> new UserModel(
                        resultSet.getInt("id"),
                        resultSet.getString("name")
                )
        );
    }
}
