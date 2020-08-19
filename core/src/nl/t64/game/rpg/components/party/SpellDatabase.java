package nl.t64.game.rpg.components.party;

import com.badlogic.gdx.Gdx;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.io.UncheckedIOException;
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
            try {
                loadSpellItems();
            } catch (IOException e) {
                throw new UncheckedIOException(e);
            }
        }
        SpellItem spellItem = spellItems.get(spellId);
        spellItem.setId(spellId);
        return new SpellItem(spellItem, rank);
    }

    private void loadSpellItems() throws IOException {
        var mapper = new ObjectMapper();
        var typeReference = new TypeReference<HashMap<String, SpellItem>>() {
        };
        String[] configFiles = Gdx.files.local(FILE_LIST).readString().split(System.lineSeparator());
        for (String filePath : configFiles) {
            String json = Gdx.files.local(SPELL_CONFIGS + filePath).readString();
            spellItems.putAll(mapper.readValue(json, typeReference));
        }
    }

}
