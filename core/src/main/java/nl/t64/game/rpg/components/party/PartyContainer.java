package nl.t64.game.rpg.components.party;

import nl.t64.game.rpg.constants.Constant;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;


public class PartyContainer {

    private static final int MAXIMUM = 5;
    private final Map<String, HeroItem> party;

    public PartyContainer() {
        this.party = new LinkedHashMap<>(MAXIMUM);
    }

    public String getHeroId(HeroItem hero) {
        return party.entrySet()
                    .stream()
                    .filter(entry -> Objects.equals(hero, entry.getValue()))
                    .findFirst()
                    .map(Map.Entry::getKey)
                    .orElseThrow(() -> {
                        throw new IllegalArgumentException(String.format("Hero %s is not in party.", hero.name));
                    });
    }

    public List<String> getAllHeroIds() {
        return List.copyOf(party.keySet());
    }

    public List<String> getAllHeroNames() {
        return party.values()
                    .stream()
                    .map(heroItem -> heroItem.name)
                    .collect(Collectors.toUnmodifiableList());
    }

    public List<Integer> getAllHeroLevels() {
        return party.values()
                    .stream()
                    .map(HeroItem::getLevel)
                    .collect(Collectors.toUnmodifiableList());
    }

    public HeroItem getHero(int index) {
        return getAllHeroes().get(index);
    }

    public List<HeroItem> getAllHeroes() {
        return List.copyOf(party.values());
    }

    public void addHero(HeroItem hero) {
        if (isFull()) {
            throw new IllegalStateException("Party is full.");
        }
        String id = hero.name.toLowerCase();
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

    public int getLastIndex() {
        return getSize() - 1;
    }

    boolean isHeroLast(HeroItem hero) {
        return getAllHeroes().indexOf(hero) == getLastIndex();
    }

    int getSize() {
        return party.size();
    }

    HeroItem getHero(String id) {
        return party.get(id);
    }

    boolean contains(String id) {
        return party.containsKey(id);
    }

}
