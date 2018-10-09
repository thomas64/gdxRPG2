package nl.t64.game.rpg.components;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import nl.t64.game.rpg.constants.Direction;
import nl.t64.game.rpg.constants.EntityState;
import nl.t64.game.rpg.entities.Entity;
import nl.t64.game.rpg.events.*;


public class NpcInputComponent extends InputComponent {

    private static final String TAG = NpcInputComponent.class.getSimpleName();

    private float stateTime = 0f;

    private EntityState state;
    private Direction direction;

    private boolean stateWasImmobile = false;
    private Direction originalDirection;

    @Override
    public void receive(Event event) {
        if (event instanceof StartStateEvent) {
            state = ((StartStateEvent) event).getState();
        }
        if (event instanceof StartDirectionEvent) {
            direction = ((StartDirectionEvent) event).getDirection();
        }
        if (event instanceof CollisionEvent) {
            stateTime = 0f;
        }
        if (event instanceof WaitEvent) {
            handleEvent((WaitEvent) event);
        }
    }

    @Override
    public void dispose() {
    }

    @Override
    public void update(Entity entity, float dt) {
        stateTime -= dt;
        if (stateTime < 0) {
            if (stateWasImmobile) {
                restoreOriginal();
            } else {
                setRandom();
            }
        }
        entity.send(new StateEvent(state));
        entity.send(new DirectionEvent(direction));
    }

    private void restoreOriginal() {
        direction = originalDirection;
        stateWasImmobile = false;
    }

    private void setRandom() {
        stateTime = MathUtils.random(1.0f, 2.0f);

        switch (state) {
            case WALKING:
                state = EntityState.IDLE;
                break;
            case IDLE:
                state = EntityState.WALKING;
                direction = Direction.getRandom();
                break;
            case IMMOBILE:
                // keep on being immobile.
                break;
            default:
                throw new IllegalArgumentException(String.format("EntityState '%s' not usable.", state));
        }
    }

    private void handleEvent(WaitEvent event) {
        Vector2 npcPosition = event.getNpcPosition();
        Vector2 playerPosition = event.getPlayerPosition();
        stateTime = 5f;
        if (state == EntityState.IMMOBILE) {
            stateWasImmobile = true;
            originalDirection = direction;
        } else if (state == EntityState.WALKING) {
            state = EntityState.IDLE;
        }
        turnToPlayer(npcPosition, playerPosition);
    }

    private void turnToPlayer(Vector2 npcPosition, Vector2 playerPosition) {
        if (npcPosition.x < playerPosition.x &&
                Math.abs(npcPosition.y - playerPosition.y) < Math.abs(npcPosition.x - playerPosition.x)) {
            direction = Direction.EAST;
        } else if (npcPosition.x > playerPosition.x &&
                Math.abs(npcPosition.y - playerPosition.y) < Math.abs(npcPosition.x - playerPosition.x)) {
            direction = Direction.WEST;
        } else if (npcPosition.y < playerPosition.y &&
                Math.abs(npcPosition.y - playerPosition.y) > Math.abs(npcPosition.x - playerPosition.x)) {
            direction = Direction.NORTH;
        } else if (npcPosition.y > playerPosition.y &&
                Math.abs(npcPosition.y - playerPosition.y) > Math.abs(npcPosition.x - playerPosition.x)) {
            direction = Direction.SOUTH;
        }
    }

}
