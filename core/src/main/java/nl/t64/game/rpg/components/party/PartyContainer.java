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

    public void addHero(HeroItem hero) throws FullException {
        if (getSize() >= MAXIMUM) {
            throw new FullException(hero);
        }
        String id = hero.getName().toLowerCase();
        party.put(id, hero);
    }

    public void removeHero(String id) throws PlayerRemovalException {
        if (id.equals(Constant.PLAYER_ID)) {
            throw new PlayerRemovalException();
        }
        party.remove(id);
    }

    public HeroItem getHero(String id) {
        return party.get(id);
    }

    int getSize() {
        return party.size();
    }

    boolean contains(String id) {
        return party.containsKey(id);
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    public static class FullException extends Exception {

        private final HeroItem hero;

        public FullException(HeroItem hero) {
            this.hero = hero;
        }

        public HeroItem getRejectedHero() {
            return this.hero;
        }
    }

    public static class PlayerRemovalException extends Exception {
    }

}
