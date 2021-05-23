package nl.t64.game.rpg.screens.world.entity;

import com.badlogic.gdx.ai.pfa.DefaultGraphPath;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import nl.t64.game.rpg.screens.world.entity.events.*;
import nl.t64.game.rpg.screens.world.pathfinding.TiledNode;


public class InputEnemy extends InputComponent {

    private static final int SECOND_NODE = 1;

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
            if (onDetectionEvent.detectionRange().contains(onDetectionEvent.ownPosition())) {
                isDetectingPlayer = true;
            }
        }
    }

    @Override
    public void dispose() {
        // empty
    }

    @Override
    public void update(Entity enemyEntity, float dt) {
        if (isDetectingPlayer) {
            isDetectingPlayer = false;
            this.enemyEntity = enemyEntity;
            setFollowPath();
        } else {
            stateTime -= dt;
            if (stateTime < 0) {
                setRandom();
            }
            enemyEntity.send(new StateEvent(state));
            enemyEntity.send(new DirectionEvent(direction));
        }
    }

    private void setFollowPath() {
        final TiledNode tiledNode = path.get(SECOND_NODE);
        final Vector2 nodePosition = new Vector2(tiledNode.x, tiledNode.y);
        final Vector2 currentGridPosition = new Vector2(enemyEntity.getPositionInGrid());
        enemyEntity.send(new StateEvent(EntityState.WALKING));
        setDirection(nodePosition, currentGridPosition);
    }

    private void setDirection(Vector2 nodePosition, Vector2 currentGridPosition) {
        if (nodePosition.y > currentGridPosition.y) {
            enemyEntity.send(new DirectionEvent(Direction.NORTH));
        } else if (nodePosition.y < currentGridPosition.y) {
            enemyEntity.send(new DirectionEvent(Direction.SOUTH));
        } else if (nodePosition.x < currentGridPosition.x) {
            enemyEntity.send(new DirectionEvent(Direction.WEST));
        } else if (nodePosition.x > currentGridPosition.x) {
            enemyEntity.send(new DirectionEvent(Direction.EAST));
        }
    }

    private void setRandom() {
        stateTime = MathUtils.random(1.0f, 2.0f);

        switch (state) {
            case WALKING:
                state = EntityState.IDLE;
                break;
            case IDLE:
                state = EntityState.WALKING;
            case FLYING:
                direction = Direction.getRandom();
                break;
            default:
                throw new IllegalArgumentException(String.format("EntityState '%s' not usable.", state));
        }
    }

}
