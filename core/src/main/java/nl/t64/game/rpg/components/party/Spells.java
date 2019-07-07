package nl.t64.game.rpg.components.party;

import lombok.NoArgsConstructor;


@NoArgsConstructor
abstract class SpellItem {
    private static final int[] TRAINING_COSTS = {20, 8, 12, 16, 20, 24, 28, 32, 36, 40, 0};
    private static final int MAXIMUM = 10;

    int rank;
    int bonus = 0;

    SpellItem(int rank) {
        this.rank = rank;
    }
}

////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

@NoArgsConstructor
class DispelNaming extends SpellItem {
    DispelNaming(int rank) {
        super(rank);
    }
}

@NoArgsConstructor
class DispelNecromancy extends SpellItem {
    DispelNecromancy(int rank) {
        super(rank);
    }
}

@NoArgsConstructor
class DispelStar extends SpellItem {
    DispelStar(int rank) {
        super(rank);
    }
}

@NoArgsConstructor
class Mirror extends SpellItem {
    Mirror(int rank) {
        super(rank);
    }
}

@NoArgsConstructor
class VsElemental extends SpellItem {
    VsElemental(int rank) {
        super(rank);
    }
}

@NoArgsConstructor
class VsNecromancy extends SpellItem {
    VsNecromancy(int rank) {
        super(rank);
    }
}

@NoArgsConstructor
class VsStar extends SpellItem {
    VsStar(int rank) {
        super(rank);
    }
}

////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

@NoArgsConstructor
class AirShield extends SpellItem {
    AirShield(int rank) {
        super(rank);
    }
}

@NoArgsConstructor
class Debilitation extends SpellItem {
    Debilitation(int rank) {
        super(rank);
    }
}

@NoArgsConstructor
class DragonFlames extends SpellItem {
    DragonFlames(int rank) {
        super(rank);
    }
}

@NoArgsConstructor
class Fireball extends SpellItem {
    Fireball(int rank) {
        super(rank);
    }
}

@NoArgsConstructor
class RemovePoison extends SpellItem {
    RemovePoison(int rank) {
        super(rank);
    }
}

@NoArgsConstructor
class StrengthPlus extends SpellItem {
    StrengthPlus(int rank) {
        super(rank);
    }
}

@NoArgsConstructor
class Wind extends SpellItem {
    Wind(int rank) {
        super(rank);
    }
}

////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

@NoArgsConstructor
class Banishing extends SpellItem {
    Banishing(int rank) {
        super(rank);
    }
}

@NoArgsConstructor
class EndurancePlus extends SpellItem {
    EndurancePlus(int rank) {
        super(rank);
    }
}

@NoArgsConstructor
class SenseAura extends SpellItem {
    SenseAura(int rank) {
        super(rank);
    }
}

@NoArgsConstructor
class Teleportation extends SpellItem {
    Teleportation(int rank) {
        super(rank);
    }
}

@NoArgsConstructor
class Weakness extends SpellItem {
    Weakness(int rank) {
        super(rank);
    }
}

////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

@NoArgsConstructor
class SolarWrath extends SpellItem {
    SolarWrath(int rank) {
        super(rank);
    }
}

@NoArgsConstructor
class StellarGravity extends SpellItem {
    StellarGravity(int rank) {
        super(rank);
    }
}

@NoArgsConstructor
class WebOfStarlight extends SpellItem {
    WebOfStarlight(int rank) {
        super(rank);
    }
}
