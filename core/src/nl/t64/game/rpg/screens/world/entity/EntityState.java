package nl.t64.game.rpg.screens.world.entity;

import com.badlogic.gdx.math.MathUtils;


public enum EntityState {
    IDLE,
    WALKING,

    IMMOBILE,
    FLOATING,
    ALIGNING,
    INVISIBLE,
    OPENED,
    RUNNING;    // only for cutscenes

    public static EntityState getRandom() {
        int randomNumber = MathUtils.random(getOnlyIDLEandWALKING());
        return EntityState.values()[randomNumber];
    }

    private static int getOnlyIDLEandWALKING() {
        return EntityState.values().length - (EntityState.values().length - 1);
    }

}
