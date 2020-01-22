package education.bert.service;

import education.bert.model.PostModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class PostSearchSmartService extends PostSearchService implements SmartService {
    private VCService vcService;
    private int postTableVersion = 0;
    private Map<Object, Integer> postVersionMap = new HashMap<>();

    private Map<Object, PostModel> posts = new HashMap<>();

    @Override
    public void setVcService(VCService vcService) {
        this.vcService = vcService;
    }

    @Override
    public synchronized void updateData() {
        int postTableActualVersion = vcService.getTableVersion("posts");
        if (postTableActualVersion != postTableVersion) {
            Map<Object, Integer> postActualVersionMap = vcService.getItemsVersion("posts");
            List<Object> changedItems = VCService.getChangedItems(postActualVersionMap, postVersionMap);

            if (changedItems.size() > posts.size()) {
                posts = super.getAll().stream().collect(Collectors.toMap(PostModel::getId, post -> post));
            } else {
                VCService.getMissedItems(postActualVersionMap, postVersionMap).forEach(o -> posts.remove(o));
                changedItems.forEach(o -> posts.put(o, super.getPost((int) o)));
            }

            postTableVersion = postTableActualVersion;
            postVersionMap = postActualVersionMap;
        }
    }

    @Override
    public PostModel getPost(int id) {
        updateData();
        return posts.get(id);
    }

    @Override
    public List<PostModel> getAll() {
        updateData();
        return new ArrayList<>(posts.values());
    }

    @Override
    public List<PostModel> searchByPostName(String postName) {
        if (postName.length() < minTextLengthForSearch) {
            throw new IllegalArgumentException("Text must contain at least " + minTextLengthForSearch + " characters");
        }
        updateData();
        return posts.values().stream().filter(post -> post.getPostName().toLowerCase().contains(postName.toLowerCase()))
                .collect(Collectors.toList());
    }

    @Override
    public List<PostModel> searchByCreator(int creatorId) {
        updateData();
        return posts.values().stream().filter(post -> post.getCreatorId() == creatorId)
                .collect(Collectors.toList());
    }
}
