package nl.t64.game.rpg.constants;

import com.badlogic.gdx.math.MathUtils;


public enum EntityState {
    IDLE,
    WALKING,

    IMMOBILE,
    ALIGNING;

    public static EntityState getRandom() {
        int randomNumber = MathUtils.random(EntityState.values().length - 3); // - 3: only IDLE and WALKING
        return EntityState.values()[randomNumber];
    }

}
