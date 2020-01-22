package education.bert.unit;

import education.bert.MyApplicationSmart;
import education.bert.model.PostModel;
import education.bert.model.UserModel;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class SmartMicroservicesTest extends MicroservicesTest {

    public SmartMicroservicesTest() {
        myApplication = new MyApplicationSmart();
    }

    @Test
    public void writeAndReadDataTest() {
        UserModel user = new UserModel(0, "Vasya");
        PostModel post = new PostModel(0, "Hello Friends", 1);
        myApplication.userUpdateService.saveUser(user);
        myApplication.postUpdateService.savePost(post);
        user.setId(1);
        post.setId(1);

        assertEquals(user, myApplication.userSearchService.getUser(1));
        assertEquals(post, myApplication.postSearchService.getPost(1));
        myApplication.dropTables();
        assertEquals(user, myApplication.userSearchService.getUser(1));
        assertEquals(post, myApplication.postSearchService.getPost(1));
    }

    @Test
    public void getAllTest() {
        UserModel user1 = new UserModel(0, "Vasya");
        UserModel user2 = new UserModel(0, "Petya");
        PostModel post1 = new PostModel(0, "Hello Friends", 1);
        PostModel post2 = new PostModel(0, "Forum Rules", 2);
        myApplication.userUpdateService.saveUser(user1);
        myApplication.userUpdateService.saveUser(user2);
        myApplication.postUpdateService.savePost(post1);
        myApplication.postUpdateService.savePost(post2);
        user1.setId(1);
        user2.setId(2);
        post1.setId(1);
        post2.setId(2);

        List<UserModel> users = myApplication.userSearchService.getAll();
        List<PostModel> posts = myApplication.postSearchService.getAll();
        assertEquals(2, users.size());
        assertEquals(2, posts.size());
        assertTrue(users.contains(user1));
        assertTrue(users.contains(user2));
        assertTrue(posts.contains(post1));
        assertTrue(posts.contains(post2));
        myApplication.dropTables();
        users = myApplication.userSearchService.getAll();
        posts = myApplication.postSearchService.getAll();
        assertEquals(2, users.size());
        assertEquals(2, posts.size());
        assertTrue(users.contains(user1));
        assertTrue(users.contains(user2));
        assertTrue(posts.contains(post1));
        assertTrue(posts.contains(post2));
    }

    @Test
    public void searchByCreatorTest() {
        PostModel post1 = new PostModel(0, "Hello Friends", 1);
        PostModel post2 = new PostModel(0, "Forum Rules", 2);
        myApplication.postUpdateService.savePost(post1);
        myApplication.postUpdateService.savePost(post2);
        post2.setId(2);

        List<PostModel> posts = myApplication.postSearchService.searchByCreator(2);
        assertEquals(1, posts.size());
        assertTrue(posts.contains(post2));
        myApplication.dropTables();
        posts = myApplication.postSearchService.searchByCreator(2);
        assertEquals(1, posts.size());
        assertTrue(posts.contains(post2));
    }
}
