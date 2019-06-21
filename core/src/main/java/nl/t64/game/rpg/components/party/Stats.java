package nl.t64.game.rpg.components.party;

import lombok.NoArgsConstructor;


@NoArgsConstructor
class Level {

    private static final int MAXIMUM = 40;

    int current;
    int hitpoints;
    int totalXp;
    int xpForUpgrades;

    Level(int current) {
        this.current = current;
        this.hitpoints = current;
        this.totalXp = getTotalXpForLevel(current);
        this.xpForUpgrades = 0;
    }

    private static int getTotalXpForLevel(float level) {
        return Math.round((5f / 6f) * (2f * level + 1f) * (level * level + level));
    }

    int getXpDeltaBetweenLevels() {
        int nextLevel = current + 1;
        return getTotalXpForLevel(nextLevel) - getTotalXpForLevel(current);
    }

    int getNeededXpForNextLevel() {
        if (current >= MAXIMUM) {
            return 1;
        } else {
            int nextLevel = current + 1;
            return getTotalXpForLevel(nextLevel) - totalXp;
        }
    }

}

@NoArgsConstructor
class Intelligence {
    private static final int MAXIMUM = 30;
    private static final float UPGRADE = 0.12f;

    int current;
    int bonus;

    Intelligence(int current) {
        this.current = current;
        this.bonus = 0;
    }

}

@NoArgsConstructor
class Strength {

    private static final int MAXIMUM = 30;
    private static final float UPGRADE = 0.12f;

    int current;
    int bonus;

    Strength(int current) {
        this.current = current;
        this.bonus = 0;
    }

}

@NoArgsConstructor
class Endurance {

    private static final int MAXIMUM = 40;
    private static final float UPGRADE = 0.12f;

    int current;
    int hitpoints;
    int bonus;

    Endurance(int current) {
        this.current = current;
        this.hitpoints = current;
        this.bonus = 0;
    }

}

@NoArgsConstructor
class Stamina {

    private static final int MAXIMUM = 90;
    private static final float UPGRADE = 0.04f;

    int current;
    int hitpoints;

    Stamina(int current) {
        this.current = current;
        this.hitpoints = current;
    }

}
