package education.bert.service;

public interface SmartService {
    void setVcService(VCService vcService);

    default void updateData() {
    }
}
