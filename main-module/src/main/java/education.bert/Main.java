package education.bert;

import education.bert.jdbc.JdbcHelper;
import education.bert.model.UserModel;
import education.bert.service.UserSearchService;
import education.bert.service.UserUpdateService;
import education.bert.service.VCService;

import java.util.List;
import java.util.Map;

public class Main {
    public static void main(String[] args) {
//        final String dbUrl = PostgresConfig.url;
//        final UserUpdateService userUpdateService = new UserUpdateService();
//        final UserSearchService userSearchService = new UserSearchService();
//
//        userUpdateService.setDbUrl(dbUrl);
//        userSearchService.setDbUrl(dbUrl);
//
//        JdbcHelper.executeUpdate(dbUrl, "DROP TABLE IF EXISTS users;");
//        JdbcHelper.executeUpdate(dbUrl, "CREATE TABLE users (id SERIAL PRIMARY KEY, name TEXT NOT NULL);");
//
//        userUpdateService.saveUser(new UserModel(0, "Vasya"));
//        userUpdateService.saveUser(new UserModel(0, "Petya"));
//        userUpdateService.saveUser(new UserModel(0, "Sasha"));
//        userUpdateService.saveUser(new UserModel(0, "Masha"));
//
//        int id = 3;
//        final List<String> list = JdbcHelper.executeQueryForList(
//                dbUrl,
//                "EXPLAIN ANALYZE SELECT id, name FROM users WHERE id = ?;",
//                statement ->
//                {
//                    statement.setInt(1, id);
//                    return statement;
//                },
//                resultSet ->
//                {
//                    return resultSet.getString(1);
//                }
//        );
//
//        System.out.println(list.size());
//        System.out.println(list);

        final VCService vcService = new VCService();

        vcService.updateTableAndItem("users", "U1");
        vcService.updateTableAndItem("users", "U2");
        vcService.updateTableAndItem("users", "U1");
//        vcService.updateTableAndItem("posts", "P1");

        Map<Object, Integer> usersVersion = vcService.getItemsVersion("users");
        System.out.println(usersVersion);

        vcService.removeItemAndUpdateTable("users", "U2");
        vcService.updateTableAndItem("users", "U1");

        Map<Object, Integer> usersNewVersion = vcService.getItemsVersion("users");
        System.out.println(usersNewVersion);

        System.out.println(VCService.getChangedItems(usersNewVersion, usersVersion));
        System.out.println(VCService.getMissedItems(usersNewVersion, usersVersion));
    }
}
