package nl.t64.game.rpg.screens.world.entity;

import com.badlogic.gdx.ai.pfa.DefaultGraphPath;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.GdxRuntimeException;
import nl.t64.game.rpg.constants.Constant;
import nl.t64.game.rpg.screens.world.entity.events.*;
import nl.t64.game.rpg.screens.world.pathfinding.TiledNode;


public class InputEnemy extends InputComponent {

    private static final int SECOND_NODE = 1;
    private static final float DETECTION_RANGE_DIVIDER = 12f;
    private static final int MINIMUM_DETECTION_RANGE = 5;
    private static final int MAXIMUM_DETECTION_RANGE = 20;

    private Entity enemyEntity;
    private float stateTime = 0f;
    private EntityState state;
    private DefaultGraphPath<TiledNode> path;
    private boolean isDetectingPlayer = false;

    @Override
    public void receive(Event event) {
        if (event instanceof LoadEntityEvent loadEvent) {
            state = loadEvent.state;
            direction = loadEvent.direction;
        }
        if (event instanceof CollisionEvent) {
            stateTime = 0f;
        }
        if (event instanceof PathUpdateEvent pathUpdateEvent) {
            path = pathUpdateEvent.path;
        }
        if (event instanceof OnDetectionEvent onDetectionEvent) {
            setIsDetectingPlayer(onDetectionEvent);
        }
    }

    private void setIsDetectingPlayer(OnDetectionEvent onDetectionEvent) {
        if (onDetectionEvent.moveSpeed() == Constant.MOVE_SPEED_4) {
            isDetectingPlayer = false;
        } else if (path != null && path.getCount() > 0
                   && path.getCount() < onDetectionEvent.moveSpeed() / DETECTION_RANGE_DIVIDER) {
            isDetectingPlayer = true;
        } else if (path != null && path.getCount() > MAXIMUM_DETECTION_RANGE) {
            isDetectingPlayer = false;
        }
        if (enemyEntity != null) {
            enemyEntity.send(new DetectionEvent(isDetectingPlayer));
        }
    }

    @Override
    public void dispose() {
        // empty
    }

    @Override
    public void update(Entity enemyEntity, float dt) {
        this.enemyEntity = enemyEntity;
        if (path.getCount() < MINIMUM_DETECTION_RANGE) {
            setIdleState();
        } else if (isDetectingPlayer) {
            setFollowPath();
        } else {
            setWandering(dt);
        }
        enemyEntity.send(new StateEvent(state));
        enemyEntity.send(new DirectionEvent(direction));
    }

    private void setIdleState() {
        state = switch (state) {
            case IDLE, WALKING -> EntityState.IDLE;
            case IDLE_ANIMATING, FLYING -> EntityState.IDLE_ANIMATING;
            default -> throw new IllegalStateException("Unexpected value: " + state);
        };
    }

    private void setFollowPath() {
        final TiledNode tiledNode = path.get(SECOND_NODE);
        final Vector2 nodePosition = new Vector2(tiledNode.x, tiledNode.y);
        final Vector2 currentGridPosition = new Vector2(enemyEntity.getPositionInGrid());
        state = getFollowState();
        direction = getFollowDirection(nodePosition, currentGridPosition);
    }

    private EntityState getFollowState() {
        return switch (state) {
            case IDLE, WALKING -> EntityState.WALKING;
            case IDLE_ANIMATING, FLYING -> EntityState.FLYING;
            default -> throw new IllegalStateException("Unexpected value: " + state);
        };
    }

    private Direction getFollowDirection(Vector2 nodePosition, Vector2 currentGridPosition) {
        if (nodePosition.y > currentGridPosition.y) {
            return Direction.NORTH;
        } else if (nodePosition.y < currentGridPosition.y) {
            return Direction.SOUTH;
        } else if (nodePosition.x < currentGridPosition.x) {
            return Direction.WEST;
        } else if (nodePosition.x > currentGridPosition.x) {
            return Direction.EAST;
        } else {
            throw new GdxRuntimeException("Is this possible?");
        }
    }

    private void setWandering(float dt) {
        stateTime -= dt;
        if (stateTime < 0) {
            setRandom();
        }
    }

    private void setRandom() {
        stateTime = MathUtils.random(1.0f, 2.0f);

        switch (state) {
            case IDLE -> {
                state = EntityState.WALKING;
                direction = Direction.getRandom();
            }
            case WALKING -> state = EntityState.IDLE;
            case FLYING -> direction = Direction.getRandom();
            case IDLE_ANIMATING -> state = EntityState.FLYING;
            default -> throw new IllegalStateException("Unexpected value: " + state);
        }
    }

}
