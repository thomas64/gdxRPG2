package nl.t64.game.rpg.components.party;

import nl.t64.game.rpg.constants.Constant;

import java.util.LinkedHashMap;
import java.util.Map;


public class PartyContainer {

    private static final int MAXIMUM = 5;
    private final Map<String, HeroItem> party;

    public PartyContainer() {
        this.party = new LinkedHashMap<>(MAXIMUM);
    }

    public void addHero(HeroItem hero) {
        if (isFull()) {
            throw new IllegalStateException("Party is full.");
        }
        String id = hero.getName().toLowerCase();
        party.put(id, hero);
    }

    public void removeHero(String id) {
        if (id.equals(Constant.PLAYER_ID)) {
            throw new IllegalArgumentException("Cannot remove player from party.");
        }
        party.remove(id);
    }

    public boolean isFull() {
        return getSize() >= MAXIMUM;
    }

    HeroItem getHero(String id) {
        return party.get(id);
    }

    int getSize() {
        return party.size();
    }

    boolean contains(String id) {
        return party.containsKey(id);
    }

}
