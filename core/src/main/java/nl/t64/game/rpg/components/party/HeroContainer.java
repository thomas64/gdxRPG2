package nl.t64.game.rpg.components.party;

import com.badlogic.gdx.Gdx;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.HashMap;
import java.util.Map;


public class HeroContainer {

    private static final String HERO_CONFIGS = "configs/characters/hero1.json";
    private final Map<String, HeroItem> heroes;

    public HeroContainer() {
        var mapper = new ObjectMapper();
        String json = Gdx.files.local(HERO_CONFIGS).readString();
        try {
            this.heroes = mapper.readValue(json, new TypeReference<HashMap<String, HeroItem>>() {
            });
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    public void addHero(HeroItem hero) {
        String id = hero.name.toLowerCase();
        heroes.put(id, hero);
    }

    public void removeHero(String id) {
        heroes.remove(id);
    }

    public HeroItem getHero(String id) {
        return heroes.get(id);
    }

    public boolean contains(String id) {
        return heroes.containsKey(id);
    }

    int getSize() {
        return heroes.size();
    }

}
