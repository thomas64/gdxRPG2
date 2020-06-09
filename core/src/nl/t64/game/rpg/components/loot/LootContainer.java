package nl.t64.game.rpg.components.loot;

import com.badlogic.gdx.Gdx;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.HashMap;
import java.util.Map;


public class LootContainer {

    private static final String LOOT_CONFIGS = "configs/loot/";
    private static final String SPARKLE_FILE_LIST = LOOT_CONFIGS + "_files_sparkles.txt";
    private static final String CHEST_FILE_LIST = LOOT_CONFIGS + "_files_chests.txt";

    private final Map<String, Loot> loot;

    public LootContainer() {
        this.loot = new HashMap<>();
        try {
            this.loadLoot();
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    public Loot getLoot(String lootId) {
        return loot.get(lootId);
    }

    private void loadLoot() throws IOException {
        var mapper = new ObjectMapper();
        var typeReference = new TypeReference<HashMap<String, Loot>>() {
        };
        for (String filePath : getFileList(SPARKLE_FILE_LIST)) {
            String json = Gdx.files.local(LOOT_CONFIGS + filePath).readString();
            loot.putAll(mapper.readValue(json, typeReference));
        }
        for (String filePath : getFileList(CHEST_FILE_LIST)) {
            String json = Gdx.files.local(LOOT_CONFIGS + filePath).readString();
            loot.putAll(mapper.readValue(json, typeReference));
        }
    }

    private String[] getFileList(String fileList) {
        return Gdx.files.local(fileList).readString().split(System.lineSeparator());
    }

}
