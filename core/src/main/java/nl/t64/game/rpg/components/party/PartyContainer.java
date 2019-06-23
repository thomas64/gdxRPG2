package nl.t64.game.rpg.components.party;

import nl.t64.game.rpg.constants.Constant;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;


public class PartyContainer {

    public static final int MAXIMUM = 6;
    private final Map<String, HeroItem> party;

    public PartyContainer() {
        this.party = new LinkedHashMap<>(MAXIMUM);
    }

    public HeroItem getHero(int index) {
        return getAllHeroes().get(index);
    }

    public int getIndex(HeroItem hero) {
        return getAllHeroes().indexOf(hero);
    }

    public List<HeroItem> getAllHeroes() {
        return List.copyOf(party.values());
    }

    public void addHero(HeroItem hero) {
        if (isFull()) {
            throw new IllegalStateException("Party is full.");
        }
        party.put(hero.id, hero);
    }

    public void removeHero(String heroId) {
        if (heroId.equals(Constant.PLAYER_ID)) {
            throw new IllegalArgumentException("Cannot remove player from party.");
        }
        party.remove(heroId);
    }

    public boolean isFull() {
        return getSize() >= MAXIMUM;
    }

    public boolean contains(HeroItem hero) {
        if (hero == null) {
            return false;
        }
        return contains(hero.id);
    }

    public boolean isHeroLast(HeroItem hero) {
        return getIndex(hero) == getLastIndex();
    }

    public int getSize() {
        return party.size();
    }

    HeroItem getHero(String heroId) {
        return party.get(heroId);
    }

    boolean contains(String heroId) {
        return party.containsKey(heroId);
    }

    private int getLastIndex() {
        return getSize() - 1;
    }

}
