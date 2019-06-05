package nl.t64.game.rpg.components.inventory;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.HashMap;
import java.util.Map;


public class InventoryDatabase {

    private static final String INVENTORY_CONFIGS = "configs/inventory";
    private static final String SUFFIX = ".json";

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

    public InventoryItem getInventoryItem(String id) {
        if (inventoryItems.isEmpty()) {
            try {
                loadInventoryItems();
            } catch (IOException e) {
                throw new UncheckedIOException(e);
            }
        }
        InventoryItem item = inventoryItems.get(id);
        item.setId(id);
        return new InventoryItem(item);
    }

    private void loadInventoryItems() throws IOException {
        var mapper = new ObjectMapper();
        FileHandle[] configFiles = Gdx.files.local(INVENTORY_CONFIGS).list(SUFFIX);
        for (FileHandle file : configFiles) {
            String json = file.readString();
            inventoryItems.putAll(mapper.readValue(json, new TypeReference<HashMap<String, InventoryItem>>() {
            }));
        }
    }

}
