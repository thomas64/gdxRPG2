package nl.t64.game.rpg.components.party;

import lombok.Getter;
import lombok.NoArgsConstructor;


@NoArgsConstructor
@Getter
class Level {

    private static final int MAXIMUM = 40;

    private int current;
    private int hitpoints;
    private int totalXp;
    private int remainingXp;

    Level(int current) {
        this.current = current;
        this.hitpoints = current;
        this.totalXp = getTotalXpForLevel(current);
        this.remainingXp = 0;
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
@Getter
class Endurance {

    private static final int MAXIMUM = 40;
    private static final float UPGRADE = 0.12f;

    private int current;
    private int hitpoints;
    private int bonus;

    Endurance(int current) {
        this.current = current;
        this.hitpoints = current;
        this.bonus = 0;
    }

}

@NoArgsConstructor
@Getter
class Stamina {

    private static final int MAXIMUM = 90;
    private static final float UPGRADE = 0.04f;

    private int current;
    private int hitpoints;

    Stamina(int current) {
        this.current = current;
        this.hitpoints = current;
    }

}
