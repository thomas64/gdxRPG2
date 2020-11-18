package nl.t64.game.rpg.components.party;

import com.fasterxml.jackson.annotation.JsonCreator;

import java.beans.ConstructorProperties;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.stream.Collectors;


class StatContainer {

    private static final int NUMBER_OF_STAT_SLOTS = 7;

    private Level level;
    private final Map<String, StatItem> stats;

    private StatContainer() {
        this.stats = new HashMap<>(NUMBER_OF_STAT_SLOTS);
    }

    @JsonCreator
    @ConstructorProperties({
            "level", "intelligence", "willpower", "dexterity",
            "agility", "endurance", "strength", "stamina"})
    private StatContainer(int lvl, int inl, int wil, int dex, int agi, int edu, int str, int sta) {
        this();
        this.level = new Level(lvl);
        this.stats.put(StatItemId.INTELLIGENCE.name(), new Intelligence(inl));
        this.stats.put(StatItemId.WILLPOWER.name(), new Willpower(wil));
        this.stats.put(StatItemId.DEXTERITY.name(), new Dexterity(dex));
        this.stats.put(StatItemId.AGILITY.name(), new Agility(agi));
        this.stats.put(StatItemId.ENDURANCE.name(), new Endurance(edu));
        this.stats.put(StatItemId.STRENGTH.name(), new Strength(str));
        this.stats.put(StatItemId.STAMINA.name(), new Stamina(sta));
    }

    int getXpNeededForNextLevel() {
        return level.getXpNeededForNextLevel();
    }

    int getXpDeltaBetweenLevels() {
        return level.getXpDeltaBetweenLevels();
    }

    void gainXp(int amount, Consumer<Boolean> hasGainedLevel) {
        level.gainXp(amount, hasGainedLevel);
    }

    int getTotalXp() {
        return level.totalXp;
    }

    int getXpToInvest() {
        return level.xpToInvest;
    }

    int getLevel() {
        return level.rank;
    }

    Map<String, Integer> getAllHpStats() {
        return Map.of("lvlRank", level.rank,
                      "lvlVari", level.variable,
                      "staRank", stats.get(StatItemId.STAMINA.name()).rank,
                      "staVari", stats.get(StatItemId.STAMINA.name()).variable,
                      "eduRank", stats.get(StatItemId.ENDURANCE.name()).rank,
                      "eduVari", stats.get(StatItemId.ENDURANCE.name()).variable,
                      "eduBon", stats.get(StatItemId.ENDURANCE.name()).bonus);
    }

    int getMaximumHp() {
        return level.rank
               + stats.get(StatItemId.STAMINA.name()).rank
               + stats.get(StatItemId.ENDURANCE.name()).rank
               + stats.get(StatItemId.ENDURANCE.name()).bonus;
    }

    int getCurrentHp() {
        return level.variable
               + stats.get(StatItemId.STAMINA.name()).variable
               + stats.get(StatItemId.ENDURANCE.name()).variable
               + stats.get(StatItemId.ENDURANCE.name()).bonus;
    }

    StatItem getById(StatItemId statItemId) {
        return stats.get(statItemId.name());
    }

    List<StatItem> getAll() {
        return Arrays.stream(StatItemId.values())
                     .map(statItemId -> stats.get(statItemId.name()))
                     .collect(Collectors.toUnmodifiableList());
    }

}
