package nl.t64.game.rpg.components.party;

import com.badlogic.gdx.Gdx;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.io.UncheckedIOException;
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

    public InventoryItem createInventoryItem(String itemId, int amount) {
        final InventoryItem inventoryItem = createInventoryItem(itemId);
        inventoryItem.setAmount(amount);
        return inventoryItem;
    }

    public InventoryItem createInventoryItem(String itemId) {
        if (inventoryItems.isEmpty()) {
            try {
                loadInventoryItems();
            } catch (IOException e) {
                throw new UncheckedIOException(e);
            }
        }
        InventoryItem inventoryItem = inventoryItems.get(itemId);
        inventoryItem.setId(itemId);
        return new InventoryItem(inventoryItem);
    }

    private void loadInventoryItems() throws IOException {
        var mapper = new ObjectMapper();
        var typeReference = new TypeReference<HashMap<String, InventoryItem>>() {
        };
        String[] configFiles = Gdx.files.local(FILE_LIST).readString().split(System.lineSeparator());
        for (String filePath : configFiles) {
            String json = Gdx.files.local(INVENTORY_CONFIGS + filePath).readString();
            inventoryItems.putAll(mapper.readValue(json, typeReference));
        }
    }

}
