package nl.t64.game.rpg.components.party;

import lombok.Getter;
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

@Getter
@NoArgsConstructor
public abstract class StatItem implements PersonalityItem {

    StatItemId id;  // Constant value
    String name;
    int maximum;    // Constant value for maximum rank possible.
    float upgrade;  // Constant value for upgrading formula.
    int rank;
    int variable;
    int bonus;

    StatItem(int rank) {
        this.rank = rank;
    }

    @Override
    public String getDescription(int totalLoremaster) {
        return getDescription() + System.lineSeparator() + System.lineSeparator()
               + getNeededXpForNextLevel();
    }

    abstract String getDescription();

    private String getNeededXpForNextLevel() {
        String xpNeeded = String.valueOf(getXpCostForNextLevel());
        if (xpNeeded.equals("0")) {
            xpNeeded = "Max";
        }
        return "XP needed for next level: " + xpNeeded;
    }

    int getXpCostForNextLevel() {
        if (rank >= maximum) {
            return 0;
        }
        int nextLevel = rank + 1;
        return Math.round(upgrade * (nextLevel * nextLevel));
    }
}

@NoArgsConstructor
class Intelligence extends StatItem {
    Intelligence(int rank) {
        super(rank);
        this.id = StatItemId.INTELLIGENCE;
        this.name = getClass().getSimpleName();
        this.maximum = 30;
        this.upgrade = 0.12f;
        this.bonus = 0;
    }

    @Override
    String getDescription() {
        return "Tekst en uitleg over " + name + ".";
    }
}

@NoArgsConstructor
class Willpower extends StatItem {
    Willpower(int rank) {
        super(rank);
        this.id = StatItemId.WILLPOWER;
        this.name = getClass().getSimpleName();
        this.maximum = 30;
        this.upgrade = 0.12f;
        this.bonus = 0;
    }

    @Override
    String getDescription() {
        return "Tekst en uitleg over " + name + ".";
    }
}

@NoArgsConstructor
class Dexterity extends StatItem {
    Dexterity(int rank) {
        super(rank);
        this.id = StatItemId.DEXTERITY;
        this.name = getClass().getSimpleName();
        this.maximum = 30;
        this.upgrade = 0.24f;
        this.bonus = 0;
    }

    @Override
    String getDescription() {
        return "Tekst en uitleg over " + name + ".";
    }
}

@NoArgsConstructor
class Agility extends StatItem {
    Agility(int rank) {
        super(rank);
        this.id = StatItemId.AGILITY;
        this.name = getClass().getSimpleName();
        this.maximum = 30;
        this.upgrade = 0.24f;
        this.bonus = 0;
    }

    @Override
    String getDescription() {
        return "Tekst en uitleg over " + name + ".";
    }
}

@NoArgsConstructor
class Strength extends StatItem {
    Strength(int rank) {
        super(rank);
        this.id = StatItemId.STRENGTH;
        this.name = getClass().getSimpleName();
        this.maximum = 30;
        this.upgrade = 0.12f;
        this.bonus = 0;
    }

    @Override
    String getDescription() {
        return "Tekst en uitleg over " + name + ".";
    }
}

@NoArgsConstructor
class Endurance extends StatItem {
    Endurance(int rank) {
        super(rank);
        this.id = StatItemId.ENDURANCE;
        this.name = getClass().getSimpleName();
        this.maximum = 40;
        this.upgrade = 0.12f;
        this.variable = rank;
        this.bonus = 0;
    }

    @Override
    String getDescription() {
        return "Tekst en uitleg over " + name + ".";
    }
}

@NoArgsConstructor
class Stamina extends StatItem {
    Stamina(int rank) {
        super(rank);
        this.id = StatItemId.STAMINA;
        this.name = getClass().getSimpleName();
        this.maximum = 90;
        this.upgrade = 0.04f;
        this.variable = rank;
    }

    @Override
    String getDescription() {
        return "Tekst en uitleg over " + name + ".";
    }
}
