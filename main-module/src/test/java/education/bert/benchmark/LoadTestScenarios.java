package education.bert.benchmark;

import education.bert.MyApplication;
import education.bert.model.PostModel;
import education.bert.model.UserModel;
import org.openjdk.jmh.infra.Blackhole;

import java.util.List;

public class LoadTestScenarios {

    public static void addSomeInitialData(MyApplication app) {
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

        app.postUpdateService.savePost(new PostModel(0, "Hello Friends", 1));
        app.postUpdateService.savePost(new PostModel(0, "Forum Rules", 2));
        app.postUpdateService.savePost(new PostModel(0, "General Discussion", 1));
        app.postUpdateService.savePost(new PostModel(0, "FAQ", 2));
        app.postUpdateService.savePost(new PostModel(0, "News", 1));
        app.postUpdateService.savePost(new PostModel(0, "Searching Teammates", 7));
        app.postUpdateService.savePost(new PostModel(0, "Please Help Me", 4));
        app.postUpdateService.savePost(new PostModel(0, "Technical Issues", 1));
        app.postUpdateService.savePost(new PostModel(0, "Guides", 2));
        app.postUpdateService.savePost(new PostModel(0, "Happy New Year", 7));
    }

    public static List<UserModel> getAllUsers(MyApplication app) {
        return app.userSearchService.getAll();
    }

    public static List<PostModel> getAllPosts(MyApplication app) {
        return app.postSearchService.getAll();
    }

    public static void particularLoadTestScenario(MyApplication app, Blackhole blackhole) {
        List<UserModel> users = app.userSearchService.getAll();
        List<PostModel> posts = app.postSearchService.getAll();
        int usersCount = users.size();
        int postsCount = posts.size();
        UserModel userOne = app.userSearchService.getUser(1);
        List<PostModel> userOnePosts = app.postSearchService.searchByCreator(1);
        int postsCountForUserOne = userOnePosts.size();

        PostModel newPost = app.postUpdateService.savePost(new PostModel(0, "Worst forum ever...", 10));

        PostModel readingNewPost = app.postSearchService.getPost(newPost.getId());
        UserModel userTwo = app.userSearchService.getUser(2);
        PostModel postTwo = app.postSearchService.getPost(2);
        List<PostModel> userSevenPosts = app.postSearchService.searchByCreator(7);
        int postsCountForUserSeven = userSevenPosts.size();

        app.userUpdateService.saveUser(new UserModel(0, "Guest"));

        users = app.userSearchService.getAll();
        int usersCountAgain = users.size();
        PostModel postTen = app.postSearchService.getPost(10);
        UserModel userFour = app.userSearchService.getUser(4);
        PostModel postSix = app.postSearchService.getPost(6);

        blackhole.consume(usersCount);
        blackhole.consume(postsCount);
        blackhole.consume(userOne);
        blackhole.consume(postsCountForUserOne);
        blackhole.consume(newPost);
        blackhole.consume(readingNewPost);
        blackhole.consume(userTwo);
        blackhole.consume(postTwo);
        blackhole.consume(postsCountForUserSeven);
        blackhole.consume(usersCountAgain);
        blackhole.consume(postTen);
        blackhole.consume(userFour);
        blackhole.consume(postSix);
    }

    public static void randomLoadTestScenario(MyApplication app, Blackhole blackhole) {
        List<UserModel> users = app.userSearchService.getAll();
        List<PostModel> posts = app.postSearchService.getAll();
        int usersCount = users.size();
        int postsCount = posts.size();
        int randomCase;
        int randomId;

        for (int i = 0; i < 10; i++) {
            randomCase = (int) (3 * Math.random());
            switch (randomCase) {
                case 0:
                    randomId = (int) (usersCount * Math.random() + 1);
                    blackhole.consume(app.userSearchService.getUser(randomId));
                    break;
                case 1:
                    randomId = (int) (postsCount * Math.random() + 1);
                    blackhole.consume(app.postSearchService.getPost(randomId));
                    break;
                case 2:
                    randomId = (int) (usersCount * Math.random() + 1);
                    blackhole.consume(app.postSearchService.searchByCreator(randomId));
                    break;
            }
        }

        randomCase = (int) (3 * Math.random());
        switch (randomCase) {
            case 0:
                app.userUpdateService.saveUser(new UserModel(0, "new User"));
                break;
            case 1:
                randomId = (int) (usersCount * Math.random() + 1);
                app.userUpdateService.saveUser(new UserModel(randomId, "renamed User"));
                break;
            case 2:
                randomId = (int) (usersCount * Math.random() + 1);
                app.userUpdateService.removeUser(randomId);
                break;
        }

        randomCase = (int) (3 * Math.random());
        switch (randomCase) {
            case 0:
                randomId = (int) (usersCount * Math.random() + 1);
                app.postUpdateService.savePost(new PostModel(0, "new Post", randomId));
                break;
            case 1:
                randomId = (int) (postsCount * Math.random() + 1);
                PostModel post = app.postSearchService.getPost(randomId);
                if (post != null) {
                    post.setPostName("changed Post");
                    app.postUpdateService.savePost(post);
                }
                break;
            case 2:
                randomId = (int) (postsCount * Math.random() + 1);
                app.postUpdateService.removePost(randomId);
                break;
        }
    }
}
