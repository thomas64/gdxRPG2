package nl.t64.game.rpg.screens.world.pathfinding;

import com.badlogic.gdx.math.Vector2;
import nl.t64.game.rpg.Utils;
import nl.t64.game.rpg.screens.world.entity.Direction;
import nl.t64.game.rpg.subjects.BlockSubject;


public class PathfindingObstacleChecker {

    private final BlockSubject blockers;
    private final float x;
    private final float y;
    private final Direction direction;

    public PathfindingObstacleChecker(Vector2 positionInGrid, Direction direction) {
        this.blockers = Utils.getBrokerManager().blockObservers;
        this.x = positionInGrid.x;
        this.y = positionInGrid.y;
        this.direction = direction;
    }

    public Direction getNewDirection() {
        return switch (direction) {
            case SOUTH -> getDirectionWhenBlockersAreSouth();
            case NORTH -> getDirectionWhenBlockersAreNorth();
            case WEST -> getDirectionWhenBlockersAreWest();
            case EAST -> getDirectionWhenBlockersAreEast();
            case NONE -> throw new IllegalArgumentException("Direction 'NONE' is not usable.");
        };
    }

    private Direction getDirectionWhenBlockersAreSouth() {
        if (blockers.isBlockerBlockingGridPoint(x + 1, y - 1)) {
            return Direction.WEST;
        } else if (blockers.isBlockerBlockingGridPoint(x - 1, y - 1)) {
            return Direction.EAST;
        } else {
            return Direction.SOUTH;
        }
    }

    private Direction getDirectionWhenBlockersAreNorth() {
        if (blockers.isBlockerBlockingGridPoint(x + 1, y + 1)) {
            return Direction.WEST;
        } else if (blockers.isBlockerBlockingGridPoint(x - 1, y + 1)) {
            return Direction.EAST;
        } else {
            return Direction.NORTH;
        }
    }

    private Direction getDirectionWhenBlockersAreWest() {
        if (blockers.isBlockerBlockingGridPoint(x - 1, y + 1)) {
            return Direction.SOUTH;
        } else if (blockers.isBlockerBlockingGridPoint(x - 1, y - 1)) {
            return Direction.NORTH;
        } else {
            return Direction.WEST;
        }
    }

    private Direction getDirectionWhenBlockersAreEast() {
        if (blockers.isBlockerBlockingGridPoint(x + 1, y + 1)) {
            return Direction.SOUTH;
        } else if (blockers.isBlockerBlockingGridPoint(x + 1, y - 1)) {
            return Direction.NORTH;
        } else {
            return Direction.EAST;
        }
    }

}
