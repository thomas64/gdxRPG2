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
        this.heroes.forEach((heroId, hero) -> hero.setId(heroId));
    }

    public void addHero(HeroItem hero) {
        heroes.put(hero.id, hero);
    }

    public void removeHero(String heroId) {
        heroes.remove(heroId);
    }

    public HeroItem getHero(String heroId) {
        return heroes.get(heroId);
    }

    public boolean contains(String heroId) {
        return heroes.containsKey(heroId);
    }

    int getSize() {
        return heroes.size();
    }

}
