package education.bert.unit;

import education.bert.MyApplication;
import education.bert.model.UserModel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class MyApplicationTest {
    final MyApplication myApplication = new MyApplication();

    @BeforeEach
    public void setup() {
        myApplication.setup();
    }

    @Test
    public void writeAndReadDataTest() {
        UserModel user = new UserModel(0, "Vasya");
        myApplication.userUpdateService.saveUser(user);
        user.setId(1);
        assertEquals(user, myApplication.userSearchService.getUser(1));
    }

    @Test
    public void removeDataTest() {
        myApplication.userUpdateService.saveUser(new UserModel(0, "Vasya"));
        myApplication.userUpdateService.removeUser(1);
        assertNull(myApplication.userSearchService.getUser(1));
    }

    @Test
    public void getAllTest() {
        UserModel user1 = new UserModel(0, "Vasya");
        UserModel user2 = new UserModel(0, "Petya");
        myApplication.userUpdateService.saveUser(user1);
        myApplication.userUpdateService.saveUser(user2);
        user1.setId(1);
        user2.setId(2);

        List<UserModel> all = myApplication.userSearchService.getAll();
        assertEquals(2, all.size());
        assertTrue(all.contains(user1));
        assertTrue(all.contains(user2));
    }

    @Test
    public void searchByNameTest() {
        UserModel user1 = new UserModel(0, "Vasya");
        UserModel user2 = new UserModel(0, "Petya");
        myApplication.userUpdateService.saveUser(user1);
        myApplication.userUpdateService.saveUser(user2);
        user1.setId(1);

        List<UserModel> results = myApplication.userSearchService.searchByName("vas");
        assertEquals(1, results.size());
        assertTrue(results.contains(user1));
    }

    @Test
    public void writeAndReadDataSmartTest() {
        UserModel user = new UserModel(0, "Vasya");
        myApplication.userUpdateSmartService.saveUser(user);
        user.setId(1);

        assertEquals(1, myApplication.vcService.getTableVersion("users"));
        Map<Object, Integer> itemsVersion = myApplication.vcService.getItemsVersion("users");
        assertEquals(1, itemsVersion.get(1));

        assertEquals(user, myApplication.userSearchSmartService.getUser(1));
        myApplication.dropTables();
        assertEquals(user, myApplication.userSearchSmartService.getUser(1));
    }

    @Test
    public void removeDataSmartTest() {
        myApplication.userUpdateSmartService.saveUser(new UserModel(0, "Vasya"));
        myApplication.userSearchSmartService.getUser(1);
        myApplication.userUpdateSmartService.removeUser(1);
        assertEquals(2, myApplication.vcService.getTableVersion("users"));
        assertTrue(myApplication.vcService.getItemsVersion("users").isEmpty());
        assertNull(myApplication.userSearchSmartService.getUser(1));
    }

    @Test
    public void getAllSmartTest() {
        UserModel user1 = new UserModel(0, "Vasya");
        UserModel user2 = new UserModel(0, "Petya");
        myApplication.userUpdateSmartService.saveUser(user1);
        myApplication.userUpdateSmartService.saveUser(user2);
        user1.setId(1);
        user2.setId(2);

        assertEquals(2, myApplication.vcService.getTableVersion("users"));
        Map<Object, Integer> itemsVersion = myApplication.vcService.getItemsVersion("users");
        assertEquals(1, itemsVersion.get(1));
        assertEquals(1, itemsVersion.get(2));

        List<UserModel> all = myApplication.userSearchSmartService.getAll();
        assertEquals(2, all.size());
        assertTrue(all.contains(user1));
        assertTrue(all.contains(user2));
        myApplication.dropTables();
        all = myApplication.userSearchSmartService.getAll();
        assertEquals(2, all.size());
        assertTrue(all.contains(user1));
        assertTrue(all.contains(user2));
    }

    @Test
    public void searchByNameSmartTest() {
        UserModel user1 = new UserModel(0, "Vasya");
        UserModel user2 = new UserModel(0, "Petya");
        myApplication.userUpdateSmartService.saveUser(user1);
        myApplication.userUpdateSmartService.saveUser(user2);
        user1.setId(1);

        myApplication.userSearchSmartService.getAll();
        myApplication.dropTables();
        List<UserModel> results = myApplication.userSearchSmartService.searchByName("vas");
        assertEquals(1, results.size());
        assertTrue(results.contains(user1));
    }
}
