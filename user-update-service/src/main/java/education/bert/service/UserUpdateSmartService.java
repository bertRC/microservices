package education.bert.service;

import education.bert.model.UserModel;

public class UserUpdateSmartService extends UserUpdateService implements SmartService {
    private VCService vcService;

    @Override
    public void setVcService(VCService vcService) {
        this.vcService = vcService;
    }

    @Override
    public UserModel saveUser(UserModel user) {
        UserModel result = super.saveUser(user);
        if (result != null) {
            vcService.updateTableAndItem("users", result.getId());
        }
        return result;
    }

    @Override
    public boolean removeUser(int id) {
        boolean isRemoved = super.removeUser(id);
        if (isRemoved) {
            vcService.removeItemAndUpdateTable("users", id);
        }
        return isRemoved;
    }
}
