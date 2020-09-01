package nl.t64.game.rpg.components.loot;

import com.badlogic.gdx.Gdx;
import nl.t64.game.rpg.Utils;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;


public class LootContainer {

    private static final String LOOT_CONFIGS = "configs/loot/";
    private static final String SPARKLE_FILE_LIST = LOOT_CONFIGS + "_files_sparkles.txt";
    private static final String CHEST_FILE_LIST = LOOT_CONFIGS + "_files_chests.txt";
    private static final String QUEST_FILE_LIST = LOOT_CONFIGS + "_files_quests.txt";

    private final Map<String, Loot> loot;

    public LootContainer() {
        this.loot = new HashMap<>();
        this.loadLoot();
    }

    public Loot getLoot(String lootId) {
        if (loot.containsKey(lootId)) {
            return loot.get(lootId);
        } else {
            return new Loot();
        }
    }

    private void loadLoot() {
        String[] sparkles = getFileList(SPARKLE_FILE_LIST);
        String[] chests = getFileList(CHEST_FILE_LIST);
        String[] quests = getFileList(QUEST_FILE_LIST);
        Stream.of(sparkles, chests, quests)
              .flatMap(Stream::of)
              .forEach(this::putInContainer);
    }

    private String[] getFileList(String fileList) {
        return Gdx.files.local(fileList).readString().split(System.lineSeparator());
    }

    private void putInContainer(String filePath) {
        String json = Gdx.files.local(LOOT_CONFIGS + filePath).readString();
        loot.putAll(Utils.readValue(json, Loot.class));
    }

}
