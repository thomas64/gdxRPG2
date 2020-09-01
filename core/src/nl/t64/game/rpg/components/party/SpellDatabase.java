package nl.t64.game.rpg.components.party;

import com.badlogic.gdx.Gdx;
import nl.t64.game.rpg.Utils;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;


public final class SpellDatabase {

    private static final String SPELL_CONFIGS = "configs/spells/";
    private static final String FILE_LIST = SPELL_CONFIGS + "_files.txt";

    private static SpellDatabase instance;

    private final Map<String, SpellItem> spellItems;

    private SpellDatabase() {
        this.spellItems = new HashMap<>();
    }

    public static SpellDatabase getInstance() {
        if (instance == null) {
            instance = new SpellDatabase();
        }
        return instance;
    }

    public SpellItem createSpellItem(String spellId, int rank) {
        if (spellItems.isEmpty()) {
            loadSpellItems();
        }
        SpellItem spellItem = spellItems.get(spellId);
        return new SpellItem(spellItem, rank);
    }

    private void loadSpellItems() {
        String[] configFiles = Gdx.files.local(FILE_LIST).readString().split(System.lineSeparator());
        Arrays.stream(configFiles)
              .map(filePath -> Gdx.files.local(SPELL_CONFIGS + filePath).readString())
              .map(json -> Utils.readValue(json, SpellItem.class))
              .forEach(spellItems::putAll);
        spellItems.forEach((spellItemId, spellItem) -> spellItem.id = spellItemId);
    }

}
