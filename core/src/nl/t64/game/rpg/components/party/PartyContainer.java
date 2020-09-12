package nl.t64.game.rpg.components.party;

import nl.t64.game.rpg.constants.Constant;
import nl.t64.game.rpg.constants.SkillItemId;

import java.util.Comparator;
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

    public HeroItem getPreviousHero(HeroItem hero) {
        if (isHeroFirst(hero)) {
            return getLastHero();
        } else {
            return getHero(getIndex(hero) - 1);
        }
    }

    public HeroItem getNextHero(HeroItem hero) {
        if (isHeroLast(hero)) {
            return getFirstHero();
        } else {
            return getHero(getIndex(hero) + 1);
        }
    }

    public void addHero(HeroItem hero) {
        if (isFull()) {
            throw new IllegalStateException("Party is full.");
        }
        party.put(hero.id, hero);
        hero.hasBeenRecruited = true;
    }

    public void removeHero(String heroId) {
        if (isPlayer(heroId)) {
            throw new IllegalArgumentException("Cannot remove player from party.");
        }
        party.remove(heroId);
    }

    public boolean isPlayer(String heroId) {
        return heroId.equals(Constant.PLAYER_ID);
    }

    public boolean isFull() {
        return getSize() >= MAXIMUM;
    }

    public boolean containsExactlyEqualTo(HeroItem candidateHeroObject) {
        if (candidateHeroObject == null) {
            return false;
        }
        final HeroItem possibleFoundHero = getHero(candidateHeroObject.id);
        return possibleFoundHero != null && possibleFoundHero.equals(candidateHeroObject);
    }

    public boolean isHeroLast(HeroItem hero) {
        return getIndex(hero) == getLastIndex();
    }

    private boolean isHeroFirst(HeroItem hero) {
        return getIndex(hero) == 0;
    }

    public int getSize() {
        return party.size();
    }

    public int getSumOfSkill(SkillItemId skillItemId) {
        return getAllHeroes().stream()
                             .mapToInt(heroItem -> heroItem.getCalculatedTotalSkillOf(skillItemId))
                             .sum();
    }

    public HeroItem getHeroWithHighestSkill(SkillItemId skillItemId) {
        return getAllHeroes().stream()
                             .max(Comparator.comparing(heroItem -> heroItem.getCalculatedTotalSkillOf(skillItemId)))
                             .orElse(getFirstHero());
    }

    HeroItem getHero(String heroId) {
        return party.get(heroId);
    }

    boolean contains(String heroId) {
        return party.containsKey(heroId);
    }

    private HeroItem getFirstHero() {
        return getHero(0);
    }

    private HeroItem getLastHero() {
        return getHero(getLastIndex());
    }

    private int getLastIndex() {
        return getSize() - 1;
    }

}
