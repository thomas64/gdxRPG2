package nl.t64.game.rpg.components.party;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.HashMap;
import java.util.Map;


public final class InventoryDatabase {

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

    public InventoryItem getInventoryItem(String itemId) {
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
        FileHandle[] configFiles = Gdx.files.local(INVENTORY_CONFIGS).list(SUFFIX);
        TypeReference<HashMap<String, InventoryItem>> typeReference = new TypeReference<>() {
        };
        for (FileHandle file : configFiles) {
            String json = file.readString();
            inventoryItems.putAll(mapper.readValue(json, typeReference));
        }
    }

}