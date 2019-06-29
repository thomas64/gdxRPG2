package nl.t64.game.rpg.components.party;

import lombok.NoArgsConstructor;


@NoArgsConstructor
class Stealth {
    private static final int MAXIMUM = 10;
    private static final float UPGRADE = 4f;

    int current;
    int bonus;

    Stealth(int current) {
        this.current = current;
        this.bonus = 0;
    }

}

@NoArgsConstructor
class Hafted {
    private static final int MAXIMUM = 10;
    private static final float UPGRADE = 3.2f;

    int current;

    Hafted(int current) {
        this.current = current;
    }

}

@NoArgsConstructor
class Pole {
    private static final int MAXIMUM = 10;
    private static final float UPGRADE = 3.2f;

    int current;

    Pole(int current) {
        this.current = current;
    }

}

@NoArgsConstructor
class Shield {
    private static final int MAXIMUM = 10;
    private static final float UPGRADE = 4f;

    int current;

    Shield(int current) {
        this.current = current;
    }

}

@NoArgsConstructor
class Sword {
    private static final int MAXIMUM = 10;
    private static final float UPGRADE = 4.8f;

    int current;

    Sword(int current) {
        this.current = current;
    }

}
