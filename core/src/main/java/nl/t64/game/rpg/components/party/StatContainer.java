package nl.t64.game.rpg.components.party;

import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.Setter;

import java.beans.ConstructorProperties;
import java.util.HashMap;
import java.util.Map;


@Setter
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
        this.stats.put(StatType.INTELLIGENCE.name(), new Intelligence(inl));
        this.stats.put(StatType.WILLPOWER.name(), new Willpower(wil));
        this.stats.put(StatType.DEXTERITY.name(), new Dexterity(dex));
        this.stats.put(StatType.AGILITY.name(), new Agility(agi));
        this.stats.put(StatType.ENDURANCE.name(), new Endurance(edu));
        this.stats.put(StatType.STRENGTH.name(), new Strength(str));
        this.stats.put(StatType.STAMINA.name(), new Stamina(sta));
    }

    int getXpNeededForNextLevel() {
        return level.getXpNeededForNextLevel();
    }

    int getXpDeltaBetweenLevels() {
        return level.getXpDeltaBetweenLevels();
    }

    int getTotalXp() {
        return level.totalXp;
    }

    int getXpRemaining() {
        return level.xpForUpgrades;
    }

    int getLevel() {
        return level.rank;
    }

    Map<String, Integer> getAllHpStats() {
        return Map.of("lvlRank", level.rank,
                      "lvlVari", level.variable,
                      "staRank", stats.get(StatType.STAMINA.name()).rank,
                      "staVari", stats.get(StatType.STAMINA.name()).variable,
                      "eduRank", stats.get(StatType.ENDURANCE.name()).rank,
                      "eduVari", stats.get(StatType.ENDURANCE.name()).variable,
                      "eduBon", stats.get(StatType.ENDURANCE.name()).bonus);
    }

    int getMaximumHp() {
        return level.rank
                + stats.get(StatType.STAMINA.name()).rank
                + stats.get(StatType.ENDURANCE.name()).rank
                + stats.get(StatType.ENDURANCE.name()).bonus;
    }

    int getCurrentHp() {
        return level.variable
                + stats.get(StatType.STAMINA.name()).variable
                + stats.get(StatType.ENDURANCE.name()).variable
                + stats.get(StatType.ENDURANCE.name()).bonus;
    }

    int getRankOf(StatType statType) {
        return stats.get(statType.name()).rank;
    }

    int getBonusStatOf(StatType statType) {
        // todo, wat te doen voor stamina? heeft wel negatieve bonus wanneer gebruikt door spells of tegenaanvallen?
        return stats.get(statType.name()).bonus;
    }

    int getXpCostForNextLevelOf(StatType statType) {
        return stats.get(statType.name()).getXpCostForNextLevel();
    }

}
