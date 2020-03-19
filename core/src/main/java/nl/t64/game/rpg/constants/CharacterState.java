package nl.t64.game.rpg.constants;

import com.badlogic.gdx.math.MathUtils;


public enum CharacterState {
    IDLE,
    WALKING,

    IMMOBILE,
    FLOATING,
    ALIGNING,
    INVISIBLE;

    public static CharacterState getRandom() {
        int randomNumber = MathUtils.random(getOnlyIDLEandWALKING());
        return CharacterState.values()[randomNumber];
    }

    private static int getOnlyIDLEandWALKING() {
        return CharacterState.values().length - 5;
    }

}
