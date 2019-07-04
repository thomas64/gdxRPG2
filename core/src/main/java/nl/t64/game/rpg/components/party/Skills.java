package nl.t64.game.rpg.components.party;

import lombok.NoArgsConstructor;


abstract class SkillItem {
    private static final int[] TRAINING_COSTS = {20, 8, 12, 16, 20, 24, 28, 32, 36, 40, 0};
    private static final int MAXIMUM = 10;

    int rank;
    int bonus = 0;
}

@NoArgsConstructor
class Alchemist extends SkillItem {
    private static final float UPGRADE = 12f;

    Alchemist(int rank) {
        super.rank = rank;
    }
}

@NoArgsConstructor
class Diplomat extends SkillItem {
    private static final float UPGRADE = 4f;

    Diplomat(int rank) {
        super.rank = rank;
    }
}

@NoArgsConstructor
class Healer extends SkillItem {
    private static final float UPGRADE = 8f;

    Healer(int rank) {
        super.rank = rank;
    }
}

@NoArgsConstructor
class Loremaster extends SkillItem {
    private static final float UPGRADE = 6f;

    Loremaster(int rank) {
        super.rank = rank;
    }
}

@NoArgsConstructor
class Mechanic extends SkillItem {
    private static final float UPGRADE = 4f;

    Mechanic(int rank) {
        super.rank = rank;
    }
}

@NoArgsConstructor
class Merchant extends SkillItem {
    private static final float UPGRADE = 6f;

    Merchant(int rank) {
        super.rank = rank;
    }
}

@NoArgsConstructor
class Ranger extends SkillItem {
    private static final float UPGRADE = 8f;

    Ranger(int rank) {
        super.rank = rank;
    }
}

@NoArgsConstructor
class Stealth extends SkillItem {
    private static final float UPGRADE = 4f;

    Stealth(int rank) {
        super.rank = rank;
    }
}

@NoArgsConstructor
class Thief extends SkillItem {
    private static final float UPGRADE = 8f;

    Thief(int rank) {
        super.rank = rank;
    }
}

@NoArgsConstructor
class Troubadour extends SkillItem {
    private static final float UPGRADE = 8f;

    Troubadour(int rank) {
        super.rank = rank;
    }
}

@NoArgsConstructor
class Warrior extends SkillItem {
    private static final float UPGRADE = 8f;

    Warrior(int rank) {
        super.rank = rank;
    }
}

@NoArgsConstructor
class Wizard extends SkillItem {
    private static final float UPGRADE = 12f;

    Wizard(int rank) {
        super.rank = rank;
    }
}

@NoArgsConstructor
class Hafted extends SkillItem {
    private static final float UPGRADE = 3.2f;

    Hafted(int rank) {
        super.rank = rank;
    }
}

@NoArgsConstructor
class Missile extends SkillItem {
    private static final float UPGRADE = 4.8f;

    Missile(int rank) {
        super.rank = rank;
    }
}

@NoArgsConstructor
class Pole extends SkillItem {
    private static final float UPGRADE = 3.2f;

    Pole(int rank) {
        super.rank = rank;
    }
}

@NoArgsConstructor
class Shield extends SkillItem {
    private static final float UPGRADE = 4f;

    Shield(int rank) {
        super.rank = rank;
    }
}

@NoArgsConstructor
class Sword extends SkillItem {
    private static final float UPGRADE = 4.8f;

    Sword(int rank) {
        super.rank = rank;
    }
}

@NoArgsConstructor
class Thrown extends SkillItem {
    private static final float UPGRADE = 3.2f;

    Thrown(int rank) {
        super.rank = rank;
    }
}
