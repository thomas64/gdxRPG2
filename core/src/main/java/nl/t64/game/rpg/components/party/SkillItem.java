package nl.t64.game.rpg.components.party;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;


@Getter
@NoArgsConstructor
public abstract class SkillItem implements PersonalityItem {
    private static final List<Integer> TRAINING_COSTS = List.of(20, 8, 12, 16, 20, 24, 28, 32, 36, 40, 0);
    private static final int MAXIMUM = 10;

    SkillItemId id; // Constant value
    String name;    // Constant value
    float upgrade;  // Constant value for upgrading formula.
    int rank;
    int bonus;

    SkillItem(int rank) {
        this.rank = rank;
        this.bonus = 0;
    }

    @Override
    public String getDescription(int totalLoremaster) {
        return getDescription() + "\n\n"
                + getNeededXpForNextLevel(totalLoremaster) + "\n"
                + getNeededGoldForNextLevel();
    }

    abstract String getDescription();

    private String getNeededXpForNextLevel(int totalLoremaster) {
        String xpNeeded = String.valueOf(getXpCostForNextLevel(totalLoremaster));
        if (xpNeeded.equals("0")) {
            xpNeeded = "Max";
        }
        return "XP needed for next level: " + xpNeeded;
    }

    private String getNeededGoldForNextLevel() {
        String goldNeeded = String.valueOf(getGoldCostForNextLevel());
        if (goldNeeded.equals("0")) {
            goldNeeded = "Max";
        }
        return "Gold needed for next level: " + goldNeeded;
    }

    int getXpCostForNextLevel(int totalLoremaster) {
        if (rank >= MAXIMUM) {
            return 0;
        }
        return Math.round(getUpgradeFormula() - ((getUpgradeFormula() / 100) * totalLoremaster));
    }

    int getGoldCostForNextLevel() {
        final int nextLevel = rank + 1;
        return TRAINING_COSTS.get(nextLevel - 1);
    }

    private float getUpgradeFormula() {
        final int nextLevel = rank + 1;
        return upgrade * (nextLevel * nextLevel);
    }

}

@NoArgsConstructor
class Alchemist extends SkillItem {
    Alchemist(int rank) {
        super(rank);
        this.id = SkillItemId.ALCHEMIST;
        this.name = getClass().getSimpleName();
        this.upgrade = 12f;
    }

    @Override
    String getDescription() {
        return "Tekst en uitleg over " + name + ".";
    }
}

@NoArgsConstructor
class Diplomat extends SkillItem {
    Diplomat(int rank) {
        super(rank);
        this.id = SkillItemId.DIPLOMAT;
        this.name = getClass().getSimpleName();
        this.upgrade = 4f;
    }

    @Override
    String getDescription() {
        return "Tekst en uitleg over " + name + ".";
    }
}

@NoArgsConstructor
class Healer extends SkillItem {
    Healer(int rank) {
        super(rank);
        this.id = SkillItemId.HEALER;
        this.name = getClass().getSimpleName();
        this.upgrade = 8f;
    }

    @Override
    String getDescription() {
        return "Tekst en uitleg over " + name + ".";
    }
}

@NoArgsConstructor
class Loremaster extends SkillItem {
    Loremaster(int rank) {
        super(rank);
        this.id = SkillItemId.LOREMASTER;
        this.name = getClass().getSimpleName();
        this.upgrade = 6f;
    }

    @Override
    String getDescription() {
        return "Tekst en uitleg over " + name + ".";
    }
}

@NoArgsConstructor
class Mechanic extends SkillItem {
    Mechanic(int rank) {
        super(rank);
        this.id = SkillItemId.MECHANIC;
        this.name = getClass().getSimpleName();
        this.upgrade = 4f;
    }

    @Override
    String getDescription() {
        return "Tekst en uitleg over " + name + ".";
    }
}

@NoArgsConstructor
class Merchant extends SkillItem {
    Merchant(int rank) {
        super(rank);
        this.id = SkillItemId.MERCHANT;
        this.name = getClass().getSimpleName();
        this.upgrade = 6f;
    }

    @Override
    String getDescription() {
        return "Tekst en uitleg over " + name + ".";
    }
}

@NoArgsConstructor
class Ranger extends SkillItem {
    Ranger(int rank) {
        super(rank);
        this.id = SkillItemId.RANGER;
        this.name = getClass().getSimpleName();
        this.upgrade = 8f;
    }

    @Override
    String getDescription() {
        return "Tekst en uitleg over " + name + ".";
    }
}

@NoArgsConstructor
class Stealth extends SkillItem {
    Stealth(int rank) {
        super(rank);
        this.id = SkillItemId.STEALTH;
        this.name = getClass().getSimpleName();
        this.upgrade = 4f;
    }

    @Override
    String getDescription() {
        return "Tekst en uitleg over " + name + ".";
    }
}

@NoArgsConstructor
class Thief extends SkillItem {
    Thief(int rank) {
        super(rank);
        this.id = SkillItemId.THIEF;
        this.name = getClass().getSimpleName();
        this.upgrade = 8f;
    }

    @Override
    String getDescription() {
        return "Tekst en uitleg over " + name + ".";
    }
}

@NoArgsConstructor
class Troubadour extends SkillItem {
    Troubadour(int rank) {
        super(rank);
        this.id = SkillItemId.TROUBADOUR;
        this.name = getClass().getSimpleName();
        this.upgrade = 8f;
    }

    @Override
    String getDescription() {
        return "Tekst en uitleg over " + name + ".";
    }
}

@NoArgsConstructor
class Warrior extends SkillItem {
    Warrior(int rank) {
        super(rank);
        this.id = SkillItemId.WARRIOR;
        this.name = getClass().getSimpleName();
        this.upgrade = 8f;
    }

    @Override
    String getDescription() {
        return "Tekst en uitleg over " + name + ".";
    }
}

@NoArgsConstructor
class Wizard extends SkillItem {
    Wizard(int rank) {
        super(rank);
        this.id = SkillItemId.WIZARD;
        this.name = getClass().getSimpleName();
        this.upgrade = 12f;
    }

    @Override
    String getDescription() {
        return "Tekst en uitleg over " + name + ".";
    }
}

@NoArgsConstructor
class Hafted extends SkillItem {
    Hafted(int rank) {
        super(rank);
        this.id = SkillItemId.HAFTED;
        this.name = getClass().getSimpleName();
        this.upgrade = 3.2f;
    }

    @Override
    String getDescription() {
        return "Tekst en uitleg over " + name + ".";
    }
}

@NoArgsConstructor
class Missile extends SkillItem {
    Missile(int rank) {
        super(rank);
        this.id = SkillItemId.MISSILE;
        this.name = getClass().getSimpleName();
        this.upgrade = 4.8f;
    }

    @Override
    String getDescription() {
        return "Tekst en uitleg over " + name + ".";
    }
}

@NoArgsConstructor
class Pole extends SkillItem {
    Pole(int rank) {
        super(rank);
        this.id = SkillItemId.POLE;
        this.name = getClass().getSimpleName();
        this.upgrade = 3.2f;
    }

    @Override
    String getDescription() {
        return "Tekst en uitleg over " + name + ".";
    }
}

@NoArgsConstructor
class Shield extends SkillItem {
    Shield(int rank) {
        super(rank);
        this.id = SkillItemId.SHIELD;
        this.name = getClass().getSimpleName();
        this.upgrade = 4f;
    }

    @Override
    String getDescription() {
        return "Tekst en uitleg over " + name + ".";
    }
}

@NoArgsConstructor
class Sword extends SkillItem {
    Sword(int rank) {
        super(rank);
        this.id = SkillItemId.SWORD;
        this.name = getClass().getSimpleName();
        this.upgrade = 4.8f;
    }

    @Override
    String getDescription() {
        return "Tekst en uitleg over " + name + ".";
    }
}

@NoArgsConstructor
class Thrown extends SkillItem {
    Thrown(int rank) {
        super(rank);
        this.id = SkillItemId.THROWN;
        this.name = getClass().getSimpleName();
        this.upgrade = 3.2f;
    }

    @Override
    String getDescription() {
        return "Tekst en uitleg over " + name + ".";
    }
}
