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
        return level.current;
    }

    Map<String, Integer> getAllHpStats() {
        return Map.of("lvlCur", level.current,
                      "lvlHp", level.hitpoints,
                      "staCur", stamina.current,
                      "staHp", stamina.hitpoints,
                      "eduCur", endurance.current,
                      "eduHp", endurance.hitpoints,
                      "eduBon", endurance.bonus);
    }

    int getMaximumHp() {
        return level.current + stamina.current + endurance.current + endurance.bonus;
    }

    int getCurrentHp() {
        return level.hitpoints + stamina.hitpoints + endurance.hitpoints + endurance.bonus;
    }

    int getOwnStatOf(StatType statType) {
        switch (statType) {
            case PROTECTION:
                return 0;
            case INTELLIGENCE:
                return intelligence.current;
            case DEXTERITY:
                return dexterity.current;
            case ENDURANCE:
                return endurance.current;
            case STRENGTH:
                return strength.current;
            case STAMINA:
                return stamina.current;
            default:
                throw new IllegalArgumentException(String.format("StatType '%s' not usable.", statType));
        }
    }

    int getBonusStatOf(StatType statType) {
        switch (statType) {
            case PROTECTION:
                return 0; // todo, er moet nog wel een bonus komen voor protection. bijv met een protection spell.
            case INTELLIGENCE:
                return intelligence.bonus;
            case DEXTERITY:
                return dexterity.bonus;
            case ENDURANCE:
                return endurance.bonus;
            case STRENGTH:
                return strength.bonus;
            case STAMINA:
                return 0;
            default:
                throw new IllegalArgumentException(String.format("StatType '%s' not usable.", statType));
        }
    }

}
