package education.bert.service;

import education.bert.model.UserModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class UserSearchSmartService extends UserSearchService implements SmartService {
    private VCService vcService;
    private int userTableVersion = 0;
    private Map<Object, Integer> userVersionMap = new HashMap<>();

    private Map<Object, UserModel> users = new HashMap<>();

    @Override
    public void setVcService(VCService vcService) {
        this.vcService = vcService;
    }

    @Override
    public synchronized void updateData() {
        int userTableActualVersion = vcService.getTableVersion("users");
        if (userTableActualVersion != userTableVersion) {
            Map<Object, Integer> userActualVersionMap = vcService.getItemsVersion("users");
            List<Object> changedItems = VCService.getChangedItems(userActualVersionMap, userVersionMap);

            if (changedItems.size() > users.size()) {
                users = super.getAll().stream().collect(Collectors.toMap(UserModel::getId, user -> user));
            } else {
                VCService.getMissedItems(userActualVersionMap, userVersionMap).forEach(o -> users.remove(o));
                changedItems.forEach(o -> users.put(o, super.getUser((int) o)));
            }

            userTableVersion = userTableActualVersion;
            userVersionMap = userActualVersionMap;
        }
    }

    @Override
    public UserModel getUser(int id) {
        updateData();
        return users.get(id);
    }

    @Override
    public synchronized List<UserModel> getAll() {
        updateData();
        return new ArrayList<>(users.values());
    }

    @Override
    public List<UserModel> searchByName(String name) {
        if (name.length() < minTextLengthForSearch) {
            throw new IllegalArgumentException("Text must contain at least " + minTextLengthForSearch + " characters");
        }
        updateData();
        return users.values().stream().filter(user -> user.getName().toLowerCase().contains(name.toLowerCase()))
                .collect(Collectors.toList());
    }
}
