package nl.t64.game.rpg.components.party;

import com.badlogic.gdx.Gdx;
import nl.t64.game.rpg.Utils;

import java.util.Map;


public class HeroContainer {

    private static final String HERO_CONFIGS = "configs/characters/hero1.json";
    private final Map<String, HeroItem> heroes;

    public HeroContainer() {
        String json = Gdx.files.local(HERO_CONFIGS).readString();
        this.heroes = Utils.readValue(json, HeroItem.class);
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
