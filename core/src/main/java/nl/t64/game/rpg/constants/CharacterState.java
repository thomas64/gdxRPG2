package nl.t64.game.rpg.constants;

import com.badlogic.gdx.math.MathUtils;


public enum CharacterState {
    IDLE,
    WALKING,

    IMMOBILE,
    ALIGNING;

    public static CharacterState getRandom() {
        int randomNumber = MathUtils.random(CharacterState.values().length - 3); // - 3: only IDLE and WALKING
        return CharacterState.values()[randomNumber];
    }

}
