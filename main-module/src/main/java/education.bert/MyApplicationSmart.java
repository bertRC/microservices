package education.bert;

import education.bert.service.*;

public class MyApplicationSmart extends MyApplication {
    public VCService vcService;

    @Override
    public void setup() {
        vcService = new VCService();
        UserUpdateSmartService userUpdateSmartService = new UserUpdateSmartService();
        UserSearchSmartService userSearchSmartService = new UserSearchSmartService();
        PostUpdateSmartService postUpdateSmartService = new PostUpdateSmartService();
        PostSearchSmartService postSearchSmartService = new PostSearchSmartService();
        userUpdateSmartService.setVcService(vcService);
        userSearchSmartService.setVcService(vcService);
        postUpdateSmartService.setVcService(vcService);
        postSearchSmartService.setVcService(vcService);

        userUpdateService = userUpdateSmartService;
        userSearchService = userSearchSmartService;
        postUpdateService = postUpdateSmartService;
        postSearchService = postSearchSmartService;

        super.setup();
    }

    public void dropTablesSmart() {
        super.dropTables();
        vcService.removeTable("users");
        vcService.removeTable("posts");
    }
}
