package nl.t64.game.rpg.components.party;

import lombok.NoArgsConstructor;


@NoArgsConstructor
class Stealth {
    private static final int MAXIMUM = 10;
    private static final float UPGRADE = 4f;

    int actual;
    int bonus;

    Stealth(int actual) {
        this.actual = actual;
        this.bonus = 0;
    }

}

@NoArgsConstructor
class Hafted {
    private static final int MAXIMUM = 10;
    private static final float UPGRADE = 3.2f;

    int actual;

    Hafted(int actual) {
        this.actual = actual;
    }

}

@NoArgsConstructor
class Pole {
    private static final int MAXIMUM = 10;
    private static final float UPGRADE = 3.2f;

    int actual;

    Pole(int actual) {
        this.actual = actual;
    }

}

@NoArgsConstructor
class Shield {
    private static final int MAXIMUM = 10;
    private static final float UPGRADE = 4f;

    int actual;

    Shield(int actual) {
        this.actual = actual;
    }

}

@NoArgsConstructor
class Sword {
    private static final int MAXIMUM = 10;
    private static final float UPGRADE = 4.8f;

    int actual;

    Sword(int actual) {
        this.actual = actual;
    }

}
