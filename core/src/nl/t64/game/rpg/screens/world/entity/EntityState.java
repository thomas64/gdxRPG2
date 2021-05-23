package nl.t64.game.rpg.screens.world.entity;

import com.badlogic.gdx.math.MathUtils;


public enum EntityState {
    IDLE,
    WALKING,

    IDLE_ANIMATING,
    FLYING,
    IMMOBILE,
    ALIGNING,
    INVISIBLE,
    OPENED,
    RUNNING;    // only for cutscenes

    public static EntityState getRandomIdleOrWalking() {
        int randomNumber = MathUtils.random(getOnlyIdleAndWalking());
        return EntityState.values()[randomNumber];
    }

    private static int getOnlyIdleAndWalking() {
        return EntityState.values().length - (EntityState.values().length - 1);
    }

}
