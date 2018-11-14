package nl.t64.game.rpg.constants;

import com.badlogic.gdx.math.MathUtils;


public enum Direction {
    NORTH,
    SOUTH,
    WEST,
    EAST,

    NONE;

    public static Direction getRandom() {
        int randomNumber = MathUtils.random(Direction.values().length - 2); // - 2: without NONE
        return Direction.values()[randomNumber];
    }
}
