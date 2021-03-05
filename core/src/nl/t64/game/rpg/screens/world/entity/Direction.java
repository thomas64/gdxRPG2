package nl.t64.game.rpg.screens.world.entity;

import com.badlogic.gdx.math.MathUtils;


public enum Direction {
    NORTH,
    SOUTH,
    WEST,
    EAST,

    NONE;

    public static Direction getRandom() {
        int randomNumber = MathUtils.random(getAllDirectionsWithoutNONE());
        return Direction.values()[randomNumber];
    }

    private static int getAllDirectionsWithoutNONE() {
        return Direction.values().length - 2;
    }

}
