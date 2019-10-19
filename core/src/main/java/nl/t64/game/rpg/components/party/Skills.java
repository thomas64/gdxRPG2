package nl.t64.game.rpg.components.party;

import lombok.NoArgsConstructor;

import java.util.List;


@NoArgsConstructor
abstract class SkillItem {
    private static final List<Integer> TRAINING_COSTS = List.of(20, 8, 12, 16, 20, 24, 28, 32, 36, 40, 0);
    private static final int MAXIMUM = 10;

    float upgrade;  // Constant value for upgrading formula.
    int rank;
    int bonus;

    SkillItem(int rank) {
        this.rank = rank;
        this.bonus = 0;
    }

    int getXpCostForNextLevel(int totalLoremaster) {
        if (!isHeroAbleToLearn()) {
            return 0;
        }
        if (rank >= MAXIMUM) {
            return 0;
        }
        return Math.round(getUpgradeFormula() - ((getUpgradeFormula() / 100) * totalLoremaster));
    }

    int getGoldCostForNextLevel() {
        if (!isHeroAbleToLearn()) {
            return 0;
        }
        final int nextLevel = rank + 1;
        return TRAINING_COSTS.get(nextLevel - 1);
    }

    int getTotal() {
        final int total = rank + bonus;
        if (total < 0 || !isHeroAbleToLearn()) {
            return 0;
        } else {
            return total;
        }
    }

    private float getUpgradeFormula() {
        final int nextLevel = rank + 1;
        return upgrade * (nextLevel * nextLevel);
    }

    private boolean isHeroAbleToLearn() {
        return rank >= 0;
    }

}

@NoArgsConstructor
class Alchemist extends SkillItem {
    Alchemist(int rank) {
        super(rank);
        this.upgrade = 12f;
    }
}

@NoArgsConstructor
class Diplomat extends SkillItem {
    Diplomat(int rank) {
        super(rank);
        this.upgrade = 4f;
    }
}

@NoArgsConstructor
class Healer extends SkillItem {
    Healer(int rank) {
        super(rank);
        this.upgrade = 8f;
    }
}

@NoArgsConstructor
class Loremaster extends SkillItem {
    Loremaster(int rank) {
        super(rank);
        this.upgrade = 6f;
    }
}

@NoArgsConstructor
class Mechanic extends SkillItem {
    Mechanic(int rank) {
        super(rank);
        this.upgrade = 4f;
    }
}

@NoArgsConstructor
class Merchant extends SkillItem {
    Merchant(int rank) {
        super(rank);
        this.upgrade = 6f;
    }
}

@NoArgsConstructor
class Ranger extends SkillItem {
    Ranger(int rank) {
        super(rank);
        this.upgrade = 8f;
    }
}

@NoArgsConstructor
class Stealth extends SkillItem {
    Stealth(int rank) {
        super(rank);
        this.upgrade = 4f;
    }
}

@NoArgsConstructor
class Thief extends SkillItem {
    Thief(int rank) {
        super(rank);
        this.upgrade = 8f;
    }
}

@NoArgsConstructor
class Troubadour extends SkillItem {
    Troubadour(int rank) {
        super(rank);
        this.upgrade = 8f;
    }
}

@NoArgsConstructor
class Warrior extends SkillItem {
    Warrior(int rank) {
        super(rank);
        this.upgrade = 8f;
    }
}

@NoArgsConstructor
class Wizard extends SkillItem {
    Wizard(int rank) {
        super(rank);
        this.upgrade = 12f;
    }
}

@NoArgsConstructor
class Hafted extends SkillItem {
    Hafted(int rank) {
        super(rank);
        this.upgrade = 3.2f;
    }
}

@NoArgsConstructor
class Missile extends SkillItem {
    Missile(int rank) {
        super(rank);
        this.upgrade = 4.8f;
    }
}

@NoArgsConstructor
class Pole extends SkillItem {
    Pole(int rank) {
        super(rank);
        this.upgrade = 3.2f;
    }
}

@NoArgsConstructor
class Shield extends SkillItem {
    Shield(int rank) {
        super(rank);
        this.upgrade = 4f;
    }
}

@NoArgsConstructor
class Sword extends SkillItem {
    Sword(int rank) {
        super(rank);
        this.upgrade = 4.8f;
    }
}

@NoArgsConstructor
class Thrown extends SkillItem {
    Thrown(int rank) {
        super(rank);
        this.upgrade = 3.2f;
    }
}
