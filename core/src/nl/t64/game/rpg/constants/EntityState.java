package nl.t64.game.rpg.constants;

import com.badlogic.gdx.math.MathUtils;


public enum EntityState {
    IDLE,
    WALKING,

    ALIGNING;

    public static EntityState getRandom() {
        int randomNumber = MathUtils.random(EntityState.values().length - 2); // - 2: ALIGNING won't be selected
        return EntityState.values()[randomNumber];
    }

}
