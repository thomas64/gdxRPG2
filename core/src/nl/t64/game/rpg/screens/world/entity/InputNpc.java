package nl.t64.game.rpg.screens.world.entity;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import nl.t64.game.rpg.screens.world.entity.events.*;


public class InputNpc extends InputComponent {

    private static final float DEFAULT_STATE_TIME = 5f;

    private float stateTime = 0f;

    private EntityState state;
    private Direction originalDirection;

    @Override
    public void receive(Event event) {
        if (event instanceof LoadEntityEvent loadEvent) {
            state = loadEvent.state;
            direction = loadEvent.direction;
            originalDirection = direction;
        }
        if (event instanceof CollisionEvent) {
            stateTime = 0f;
        }
        if (event instanceof WaitEvent waitEvent) {
            handleEvent(waitEvent);
        }
    }

    @Override
    public void dispose() {
        // empty
    }

    @Override
    public void update(Entity npcEntity, float dt) {
        stateTime -= dt;
        if (stateTime < 0) {
            setRandom();
        }
        npcEntity.send(new StateEvent(state));
        npcEntity.send(new DirectionEvent(direction));
    }

    private void setRandom() {
        stateTime = MathUtils.random(1.0f, 2.0f);

        switch (state) {
            case INVISIBLE:
                break;
            case WALKING:
                state = EntityState.IDLE;
                break;
            case IDLE:
                state = EntityState.WALKING;
                direction = Direction.getRandom();
                break;
            case IMMOBILE:
            case IDLE_ANIMATING:
                direction = originalDirection;
                break;
            default:
                throw new IllegalArgumentException(String.format("EntityState '%s' not usable.", state));
        }
    }

    private void handleEvent(WaitEvent event) {
        Vector2 npcPosition = event.npcPosition();
        Vector2 playerPosition = event.playerPosition();
        stateTime = DEFAULT_STATE_TIME;
        if (state.equals(EntityState.WALKING)) {
            state = EntityState.IDLE;
        }
        turnToPlayer(npcPosition, playerPosition);
    }

    private void turnToPlayer(Vector2 npcPosition, Vector2 playerPosition) {
        if (npcPosition.x < playerPosition.x
            && Math.abs(npcPosition.y - playerPosition.y) < Math.abs(npcPosition.x - playerPosition.x)) {
            direction = Direction.EAST;
        } else if (npcPosition.x > playerPosition.x
                   && Math.abs(npcPosition.y - playerPosition.y) < Math.abs(npcPosition.x - playerPosition.x)) {
            direction = Direction.WEST;
        } else if (npcPosition.y < playerPosition.y
                   && Math.abs(npcPosition.y - playerPosition.y) > Math.abs(npcPosition.x - playerPosition.x)) {
            direction = Direction.NORTH;
        } else if (npcPosition.y > playerPosition.y
                   && Math.abs(npcPosition.y - playerPosition.y) > Math.abs(npcPosition.x - playerPosition.x)) {
            direction = Direction.SOUTH;
        }
    }

}
