package education.bert.unit;

import education.bert.MyApplication;
import education.bert.model.PostModel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class PostServicesTest {
    final MyApplication myApplication = new MyApplication();

    @BeforeEach
    public void setup() {
        myApplication.setup();
    }

    @Test
    public void writeAndReadDataTest() {
        PostModel post = new PostModel(0, "Hello Friends", 1);
        myApplication.postUpdateService.savePost(post);
        post.setId(1);
        assertEquals(post, myApplication.postSearchService.getPost(1));
    }

    @Test
    public void removeDataTest() {
        myApplication.postUpdateService.savePost(new PostModel(0, "Hello Friends", 1));
        myApplication.postUpdateService.removePost(1);
        assertNull(myApplication.postSearchService.getPost(1));
    }

    @Test
    public void getAllTest() {
        PostModel post1 = new PostModel(0, "Hello Friends", 1);
        PostModel post2 = new PostModel(0, "Forum Rules", 2);
        myApplication.postUpdateService.savePost(post1);
        myApplication.postUpdateService.savePost(post2);
        post1.setId(1);
        post2.setId(2);

        List<PostModel> all = myApplication.postSearchService.getAll();
        assertEquals(2, all.size());
        assertTrue(all.contains(post1));
        assertTrue(all.contains(post2));
    }

    @Test
    public void searchByPostNameTest() {
        PostModel post1 = new PostModel(0, "Hello Friends", 1);
        PostModel post2 = new PostModel(0, "Forum Rules", 2);
        myApplication.postUpdateService.savePost(post1);
        myApplication.postUpdateService.savePost(post2);
        post1.setId(1);

        List<PostModel> results = myApplication.postSearchService.searchByPostName("fri");
        assertEquals(1, results.size());
        assertTrue(results.contains(post1));
    }

    @Test
    public void searchByCreatorTest() {
        PostModel post1 = new PostModel(0, "Hello Friends", 1);
        PostModel post2 = new PostModel(0, "Forum Rules", 2);
        myApplication.postUpdateService.savePost(post1);
        myApplication.postUpdateService.savePost(post2);
        post2.setId(2);

        List<PostModel> results = myApplication.postSearchService.searchByCreator(2);
        assertEquals(1, results.size());
        assertTrue(results.contains(post2));
    }

    @Test
    public void writeAndReadDataSmartTest() {
        PostModel post = new PostModel(0, "Hello Friends", 1);
        myApplication.postUpdateSmartService.savePost(post);
        post.setId(1);

        assertEquals(1, myApplication.vcService.getTableVersion("posts"));
        Map<Object, Integer> itemsVersion = myApplication.vcService.getItemsVersion("posts");
        assertEquals(1, itemsVersion.get(1));

        assertEquals(post, myApplication.postSearchSmartService.getPost(1));
        myApplication.dropTables();
        assertEquals(post, myApplication.postSearchSmartService.getPost(1));
    }

    @Test
    public void removeDataSmartTest() {
        myApplication.postUpdateSmartService.savePost(new PostModel(0, "Hello Friends", 1));
        myApplication.postSearchSmartService.getPost(1);
        myApplication.postUpdateSmartService.removePost(1);
        assertEquals(2, myApplication.vcService.getTableVersion("posts"));
        assertTrue(myApplication.vcService.getItemsVersion("posts").isEmpty());
        assertNull(myApplication.postSearchSmartService.getPost(1));
    }

    @Test
    public void getAllSmartTest() {
        PostModel post1 = new PostModel(0, "Hello Friends", 1);
        PostModel post2 = new PostModel(0, "Forum Rules", 2);
        myApplication.postUpdateSmartService.savePost(post1);
        myApplication.postUpdateSmartService.savePost(post2);
        post1.setId(1);
        post2.setId(2);

        assertEquals(2, myApplication.vcService.getTableVersion("posts"));
        Map<Object, Integer> itemsVersion = myApplication.vcService.getItemsVersion("posts");
        assertEquals(1, itemsVersion.get(1));
        assertEquals(1, itemsVersion.get(2));

        List<PostModel> all = myApplication.postSearchSmartService.getAll();
        assertEquals(2, all.size());
        assertTrue(all.contains(post1));
        assertTrue(all.contains(post2));
        myApplication.dropTables();
        all = myApplication.postSearchSmartService.getAll();
        assertEquals(2, all.size());
        assertTrue(all.contains(post1));
        assertTrue(all.contains(post2));
    }

    @Test
    public void searchByPostNameSmartTest() {
        PostModel post1 = new PostModel(0, "Hello Friends", 1);
        PostModel post2 = new PostModel(0, "Forum Rules", 2);
        myApplication.postUpdateSmartService.savePost(post1);
        myApplication.postUpdateSmartService.savePost(post2);
        post1.setId(1);

        myApplication.postSearchSmartService.getAll();
        myApplication.dropTables();
        List<PostModel> results = myApplication.postSearchSmartService.searchByPostName("fri");
        assertEquals(1, results.size());
        assertTrue(results.contains(post1));
    }

    @Test
    public void searchByCreatorSmartTest() {
        PostModel post1 = new PostModel(0, "Hello Friends", 1);
        PostModel post2 = new PostModel(0, "Forum Rules", 2);
        myApplication.postUpdateSmartService.savePost(post1);
        myApplication.postUpdateSmartService.savePost(post2);
        post2.setId(2);

        myApplication.postSearchSmartService.getAll();
        myApplication.dropTables();
        List<PostModel> results = myApplication.postSearchSmartService.searchByCreator(2);
        assertEquals(1, results.size());
        assertTrue(results.contains(post2));
    }
}
