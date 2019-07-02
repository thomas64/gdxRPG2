package nl.t64.game.rpg.components.party;

import lombok.Setter;

import java.util.Map;


@Setter
class StatContainer {

    private Level level;

    private Intelligence intelligence;
    private Dexterity dexterity;
    private Endurance endurance;
    private Strength strength;
    private Stamina stamina;

    int getNeededXpForNextLevel() {
        return level.getNeededXpForNextLevel();
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
        return level.actual;
    }

    Map<String, Integer> getAllHpStats() {
        return Map.of("lvlCur", level.actual,
                      "lvlHp", level.variable,
                      "staCur", stamina.actual,
                      "staHp", stamina.variable,
                      "eduCur", endurance.actual,
                      "eduHp", endurance.variable,
                      "eduBon", endurance.bonus);
    }

    int getMaximumHp() {
        return level.actual + stamina.actual + endurance.actual + endurance.bonus;
    }

    int getCurrentHp() {
        return level.variable + stamina.variable + endurance.variable + endurance.bonus;
    }

    int getOwnStatOf(StatType statType) {
        switch (statType) {
            case INTELLIGENCE:
                return intelligence.actual;
            case DEXTERITY:
                return dexterity.actual;
            case ENDURANCE:
                return endurance.actual;
            case STRENGTH:
                return strength.actual;
            case STAMINA:
                return stamina.actual;
            default:
                throw new IllegalArgumentException(String.format("StatType '%s' not usable.", statType));
        }
    }

    int getBonusStatOf(StatType statType) {
        switch (statType) {
            case INTELLIGENCE:
                return intelligence.bonus;
            case DEXTERITY:
                return dexterity.bonus;
            case ENDURANCE:
                return endurance.bonus;
            case STRENGTH:
                return strength.bonus;
            case STAMINA:
                return 0;   // todo, stamina heeft wel negatieve bonus wanneer gebruikt door spells of tegenaanvallen.
            default:
                throw new IllegalArgumentException(String.format("StatType '%s' not usable.", statType));
        }
    }

}
