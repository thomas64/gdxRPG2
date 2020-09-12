package nl.t64.game.rpg.components.party;

import com.badlogic.gdx.Gdx;
import nl.t64.game.rpg.Utils;
import nl.t64.game.rpg.constants.InventoryGroup;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;


public final class InventoryDatabase {

    private static final String INVENTORY_CONFIGS = "configs/inventory/";
    private static final String FILE_LIST = INVENTORY_CONFIGS + "_files.txt";
    private static final int DEFAULT_SHOP_RESOURCE_AMOUNT = 100;
    private static final int DEFAULT_SHOP_POTION_AMOUNT = 20;

    private static InventoryDatabase instance;

    private final Map<String, InventoryItem> inventoryItems;

    private InventoryDatabase() {
        this.inventoryItems = new HashMap<>();
    }

    public static InventoryDatabase getInstance() {
        if (instance == null) {
            instance = new InventoryDatabase();
        }
        return instance;
    }

    public InventoryItem createInventoryItemForShop(String itemId) {
        final InventoryItem inventoryItem = createInventoryItem(itemId);
        if (inventoryItem.group.equals(InventoryGroup.RESOURCE)) {
            inventoryItem.setAmount(DEFAULT_SHOP_RESOURCE_AMOUNT);
        } else if (inventoryItem.group.equals(InventoryGroup.POTION)) {
            inventoryItem.setAmount(DEFAULT_SHOP_POTION_AMOUNT);
        }
        return inventoryItem;
    }

    public InventoryItem createInventoryItem(String itemId) {
        return createInventoryItem(itemId, 1);
    }

    public InventoryItem createInventoryItem(String itemId, int amount) {
        if (inventoryItems.isEmpty()) {
            loadInventoryItems();
        }
        InventoryItem inventoryItem = inventoryItems.get(itemId);
        return new InventoryItem(inventoryItem, amount);
    }

    private void loadInventoryItems() {
        String[] configFiles = Gdx.files.local(FILE_LIST).readString().split(System.lineSeparator());
        Arrays.stream(configFiles)
              .map(filePath -> Gdx.files.local(INVENTORY_CONFIGS + filePath).readString())
              .map(json -> Utils.readValue(json, InventoryItem.class))
              .forEach(inventoryItems::putAll);
        inventoryItems.forEach((inventoryItemId, inventoryItem) -> inventoryItem.id = inventoryItemId);
    }

}
