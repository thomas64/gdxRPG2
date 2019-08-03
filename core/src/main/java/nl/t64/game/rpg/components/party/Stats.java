package nl.t64.game.rpg.components.party;

import lombok.NoArgsConstructor;


@NoArgsConstructor
class Level {

    private static final int MAXIMUM = 40;

    int rank;
    int variable;
    int totalXp;
    int xpToInvest;

    Level(int rank) {
        this.rank = rank;
        this.variable = rank;
        this.totalXp = getTotalXpForLevel(rank);
        this.xpToInvest = 0;
    }

    private static int getTotalXpForLevel(float level) {
        return Math.round((5f / 6f) * (2f * level + 1f) * (level * level + level));
    }

    int getXpDeltaBetweenLevels() {
        int nextLevel = rank + 1;
        return getTotalXpForLevel(nextLevel) - getTotalXpForLevel(rank);
    }

    int getXpNeededForNextLevel() {
        if (rank >= MAXIMUM) {
            return 1;
        } else {
            int nextLevel = rank + 1;
            return getTotalXpForLevel(nextLevel) - totalXp;
        }
    }

}

@NoArgsConstructor
abstract class StatItem {
    int rank;
    int variable;
    int bonus;

    StatItem(int rank) {
        this.rank = rank;
    }

    abstract int getXpCostForNextLevel();

    int getXpCostForNextLevel(int maximum, float upgrade) {
        if (rank >= maximum) {
            return 0;
        }
        int nextLevel = rank + 1;
        return Math.round(upgrade * (nextLevel * nextLevel));
    }
}

@NoArgsConstructor
class Intelligence extends StatItem {
    private static final int MAXIMUM = 30;
    private static final float UPGRADE = 0.12f;

    Intelligence(int rank) {
        super(rank);
        super.bonus = 0;
    }

    @Override
    int getXpCostForNextLevel() {
        return getXpCostForNextLevel(MAXIMUM, UPGRADE);
    }
}

@NoArgsConstructor
class Willpower extends StatItem {
    private static final int MAXIMUM = 30;
    private static final float UPGRADE = 0.12f;

    Willpower(int rank) {
        super(rank);
        super.bonus = 0;
    }

    @Override
    int getXpCostForNextLevel() {
        return getXpCostForNextLevel(MAXIMUM, UPGRADE);
    }
}

@NoArgsConstructor
class Dexterity extends StatItem {
    private static final int MAXIMUM = 30;
    private static final float UPGRADE = 0.24f;

    Dexterity(int rank) {
        super(rank);
        super.bonus = 0;
    }

    @Override
    int getXpCostForNextLevel() {
        return getXpCostForNextLevel(MAXIMUM, UPGRADE);
    }
}

@NoArgsConstructor
class Agility extends StatItem {
    private static final int MAXIMUM = 30;
    private static final float UPGRADE = 0.24f;

    Agility(int rank) {
        super(rank);
        super.bonus = 0;
    }

    @Override
    int getXpCostForNextLevel() {
        return getXpCostForNextLevel(MAXIMUM, UPGRADE);
    }
}

@NoArgsConstructor
class Strength extends StatItem {
    private static final int MAXIMUM = 30;
    private static final float UPGRADE = 0.12f;

    Strength(int rank) {
        super(rank);
        super.bonus = 0;
    }

    @Override
    int getXpCostForNextLevel() {
        return getXpCostForNextLevel(MAXIMUM, UPGRADE);
    }
}

@NoArgsConstructor
class Endurance extends StatItem {
    private static final int MAXIMUM = 40;
    private static final float UPGRADE = 0.12f;

    Endurance(int rank) {
        super(rank);
        super.variable = rank;
        super.bonus = 0;
    }

    @Override
    int getXpCostForNextLevel() {
        return getXpCostForNextLevel(MAXIMUM, UPGRADE);
    }
}

@NoArgsConstructor
class Stamina extends StatItem {
    private static final int MAXIMUM = 90;
    private static final float UPGRADE = 0.04f;

    Stamina(int rank) {
        super(rank);
        super.variable = rank;
    }

    @Override
    int getXpCostForNextLevel() {
        return getXpCostForNextLevel(MAXIMUM, UPGRADE);
    }
}
