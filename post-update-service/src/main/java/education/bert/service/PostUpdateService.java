package education.bert.service;

import education.bert.jdbc.JdbcHelper;
import education.bert.model.PostModel;

public class PostUpdateService {
    private String dbUrl;

    public void setDbUrl(String dbUrl) {
        this.dbUrl = dbUrl;
    }

    public PostModel savePost(PostModel post) {
        PostModel result = new PostModel(post.getId(), post.getPostName(), post.getCreatorId());
        if (post.getId() == 0) {
            int id = JdbcHelper.executeUpdateWithId(
                    dbUrl,
                    "INSERT INTO posts(postName, creatorId) VALUES (?, ?);",
                    statement ->
                    {
                        statement.setString(1, post.getPostName());
                        statement.setInt(2, post.getCreatorId());
                        return statement;
                    }
            );
            result.setId(id);
        } else if (0 ==
                JdbcHelper.executeUpdate(
                        dbUrl,
                        "UPDATE posts SET postName = ?, creatorId = ? WHERE id = ?;",
                        statement ->
                        {
                            statement.setString(1, post.getPostName());
                            statement.setInt(2, post.getCreatorId());
                            statement.setInt(3, post.getId());
                            return statement;
                        }
                )
        ) {
            return null;
        }
        return result;
    }

    public boolean removePost(int id) {
        return 0 != JdbcHelper.executeUpdate(
                dbUrl,
                "DELETE FROM posts WHERE id = ?;",
                statement ->
                {
                    statement.setInt(1, id);
                    return statement;
                }
        );
    }
}
