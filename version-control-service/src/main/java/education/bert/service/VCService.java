package education.bert.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

public class VCService {
    private final Map<String, Integer> tableVersionMap = new ConcurrentHashMap<>();
    private final Map<String, Map<Object, Integer>> itemVersionMap = new ConcurrentHashMap<>();

    public void updateTable(String tableName) {
        tableVersionMap.merge(tableName, 1, Integer::sum);
    }

    public void updateItem(String tableName, Object id) {
        Map<Object, Integer> innerMap = itemVersionMap.get(tableName);
        if (innerMap == null) {
            innerMap = new ConcurrentHashMap<>();
            innerMap.put(id, 1);
            itemVersionMap.put(tableName, innerMap);
        } else {
            innerMap.merge(id, 1, Integer::sum);
        }
    }

    public void updateTableAndItem(String tableName, Object id) {
        updateTable(tableName);
        updateItem(tableName, id);
    }

    public void removeTable(String tableName) {
        tableVersionMap.remove(tableName);
        itemVersionMap.remove(tableName);
    }

    public void removeItem(String tableName, Object id) {
        Map<Object, Integer> innerMap = itemVersionMap.get(tableName);
        if (innerMap != null) {
            innerMap.remove(id);
        }
    }

    public void removeItemAndUpdateTable(String tableName, Object id) {
        removeItem(tableName, id);
        updateTable(tableName);
    }

    public int getTableVersion(String tableName) {
        return tableVersionMap.getOrDefault(tableName, 0);
    }

    public Map<Object, Integer> getItemsVersion(String tableName) {
        return new HashMap<>(itemVersionMap.getOrDefault(tableName, new HashMap<>()));
    }

    public static List<Object> getChangedItems(Map<Object, Integer> actualVersions, Map<Object, Integer> versionsToCheck) {
        return actualVersions.entrySet().stream()
                .filter(e -> !e.getValue().equals(versionsToCheck.get(e.getKey())))
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());
    }

    public static List<Object> getMissedItems(Map<Object, Integer> actualVersions, Map<Object, Integer> versionsToCheck) {
        return versionsToCheck.keySet().stream()
                .filter(integer -> !actualVersions.containsKey(integer))
                .collect(Collectors.toList());
    }

//    public void printTableVersion() {
//        System.out.println(tableVersionMap);
//    }
//
//    public void printItemVersion() {
//        System.out.println(itemVersionMap);
//    }
}
