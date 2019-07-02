package nl.t64.game.rpg.components.party;

import lombok.NoArgsConstructor;


@NoArgsConstructor
class Level {

    private static final int MAXIMUM = 40;

    int actual;
    int variable;
    int totalXp;
    int xpForUpgrades;

    Level(int actual) {
        this.actual = actual;
        this.variable = actual;
        this.totalXp = getTotalXpForLevel(actual);
        this.xpForUpgrades = 0;
    }

    private static int getTotalXpForLevel(float level) {
        return Math.round((5f / 6f) * (2f * level + 1f) * (level * level + level));
    }

    int getXpDeltaBetweenLevels() {
        int nextLevel = actual + 1;
        return getTotalXpForLevel(nextLevel) - getTotalXpForLevel(actual);
    }

    int getNeededXpForNextLevel() {
        if (actual >= MAXIMUM) {
            return 1;
        } else {
            int nextLevel = actual + 1;
            return getTotalXpForLevel(nextLevel) - totalXp;
        }
    }

}

@NoArgsConstructor
class Intelligence {
    private static final int MAXIMUM = 30;
    private static final float UPGRADE = 0.12f;

    int actual;
    int bonus;

    Intelligence(int actual) {
        this.actual = actual;
        this.bonus = 0;
    }

}

@NoArgsConstructor
class Dexterity {
    private static final int MAXIMUM = 30;
    private static final float UPGRADE = 0.24f;

    int actual;
    int bonus;

    Dexterity(int actual) {
        this.actual = actual;
        this.bonus = 0;
    }
}

@NoArgsConstructor
class Strength {

    private static final int MAXIMUM = 30;
    private static final float UPGRADE = 0.12f;

    int actual;
    int bonus;

    Strength(int actual) {
        this.actual = actual;
        this.bonus = 0;
    }

}

@NoArgsConstructor
class Endurance {

    private static final int MAXIMUM = 40;
    private static final float UPGRADE = 0.12f;

    int actual;
    int variable;
    int bonus;

    Endurance(int actual) {
        this.actual = actual;
        this.variable = actual;
        this.bonus = 0;
    }

}

@NoArgsConstructor
class Stamina {

    private static final int MAXIMUM = 90;
    private static final float UPGRADE = 0.04f;

    int actual;
    int variable;

    Stamina(int actual) {
        this.actual = actual;
        this.variable = actual;
    }

}
