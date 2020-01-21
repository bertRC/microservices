package education.bert.service;

import education.bert.jdbc.JdbcHelper;
import education.bert.model.PostModel;

import java.util.List;

public class PostSearchService {
    private String dbUrl;

    public final int minTextLengthForSearch = 3;

    public void setDbUrl(String dbUrl) {
        this.dbUrl = dbUrl;
    }

    public PostModel getPost(int id) {
        return JdbcHelper.executeQueryForObject(
                dbUrl,
                "SELECT id, postName, creatorId FROM posts WHERE id = ?;",
                statement ->
                {
                    statement.setInt(1, id);
                    return statement;
                },
                resultSet -> new PostModel(
                        resultSet.getInt("id"),
                        resultSet.getString("postName"),
                        resultSet.getInt("creatorId")
                )
        ).orElse(null);
    }

    public List<PostModel> getAll() {
        return JdbcHelper.executeQueryForList(
                dbUrl,
                "SELECT id, postName, creatorId FROM posts;",
                resultSet -> new PostModel(
                        resultSet.getInt("id"),
                        resultSet.getString("postName"),
                        resultSet.getInt("creatorId")
                )
        );
    }

    public List<PostModel> searchByPostName(String postName) {
        if (postName.length() < minTextLengthForSearch) {
            throw new IllegalArgumentException("Text must contain at least " + minTextLengthForSearch + " characters");
        }
        return JdbcHelper.executeQueryForList(
                dbUrl,
                "SELECT id, postName, creatorId FROM posts WHERE LOWER(postName) LIKE ?;",
                statement ->
                {
                    statement.setString(1, "%" + postName.toLowerCase() + "%");
                    return statement;
                },
                resultSet -> new PostModel(
                        resultSet.getInt("id"),
                        resultSet.getString("postName"),
                        resultSet.getInt("creatorId")
                )
        );
    }

    public List<PostModel> searchByCreator(int creatorId) {
        return JdbcHelper.executeQueryForList(
                dbUrl,
                "SELECT id, postName, creatorId FROM posts WHERE creatorId = ?;",
                statement ->
                {
                    statement.setInt(1, creatorId);
                    return statement;
                },
                resultSet -> new PostModel(
                        resultSet.getInt("id"),
                        resultSet.getString("postName"),
                        resultSet.getInt("creatorId")
                )
        );
    }
}
