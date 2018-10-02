package nl.t64.game.rpg.constants;

import com.badlogic.gdx.math.MathUtils;


public enum Direction {
    NORTH,
    SOUTH,
    WEST,
    EAST;

    public static Direction getRandom() {
        int randomNumber = MathUtils.random(Direction.values().length - 1);
        return Direction.values()[randomNumber];
    }
}
