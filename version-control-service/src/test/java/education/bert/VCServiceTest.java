package education.bert;

import education.bert.service.VCService;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class VCServiceTest {
    private VCService vcService = new VCService();

    @Test
    public void updateTableTest() {
        assertEquals(0, vcService.getTableVersion("table"));
        vcService.updateTable("table");
        assertEquals(1, vcService.getTableVersion("table"));
    }

    @Test
    public void updateItemTest() {
        Map<Object, Integer> expectedMap = new HashMap<>();
        expectedMap.put("N1", 1);
        assertTrue(vcService.getItemsVersion("table").isEmpty());
        vcService.updateItem("table", "N1");
        assertEquals(expectedMap, vcService.getItemsVersion("table"));
    }

    @Test
    public void removeTableTest() {
        vcService.updateTableAndItem("table", "N1");
        vcService.removeTable("table");
        assertEquals(0, vcService.getTableVersion("table"));
        assertTrue(vcService.getItemsVersion("table").isEmpty());
    }

    @Test
    public void removeItemTest() {
        vcService.updateItem("table", "N1");
        vcService.removeItem("table", "N1");
        assertTrue(vcService.getItemsVersion("table").isEmpty());
    }

    @Test
    public void removeItemAndUpdateTableTest() {
        vcService.updateTableAndItem("table", "N1");
        vcService.removeItemAndUpdateTable("table", "N1");
        assertEquals(2, vcService.getTableVersion("table"));
        assertTrue(vcService.getItemsVersion("table").isEmpty());
    }

    @Test
    public void getChangedItemsTest() {
        vcService.updateTableAndItem("table", "N1");

        Map<Object, Integer> tableBefore = vcService.getItemsVersion("table");
        vcService.updateTableAndItem("table", "N2");
        vcService.updateTableAndItem("table", "N3");
        Map<Object, Integer> tableAfter = vcService.getItemsVersion("table");

        List<Object> changedItems = VCService.getChangedItems(tableAfter, tableBefore);
        assertEquals(2, changedItems.size());
        assertTrue(changedItems.contains("N2"));
        assertTrue(changedItems.contains("N3"));
    }

    @Test
    public void getMissedItemsTest() {
        vcService.updateTableAndItem("table", "N1");
        vcService.updateTableAndItem("table", "N2");
        vcService.updateTableAndItem("table", "N3");

        Map<Object, Integer> tableBefore = vcService.getItemsVersion("table");
        vcService.removeItemAndUpdateTable("table", "N2");
        vcService.removeItemAndUpdateTable("table", "N3");
        Map<Object, Integer> tableAfter = vcService.getItemsVersion("table");

        List<Object> missedItems = VCService.getMissedItems(tableAfter, tableBefore);
        assertTrue(missedItems.contains("N2"));
        assertTrue(missedItems.contains("N3"));
    }
}
