package education.bert.service;

import education.bert.model.PostModel;

public class PostUpdateSmartService extends PostUpdateService implements SmartService {
    private VCService vcService;

    @Override
    public void setVcService(VCService vcService) {
        this.vcService = vcService;
    }

    @Override
    public PostModel savePost(PostModel post) {
        PostModel result = super.savePost(post);
        if (result != null) {
            vcService.updateTableAndItem("posts", result.getId());
        }
        return result;
    }

    @Override
    public boolean removePost(int id) {
        boolean isRemoved = super.removePost(id);
        if (isRemoved) {
            vcService.removeItemAndUpdateTable("posts", id);
        }
        return isRemoved;
    }
}
