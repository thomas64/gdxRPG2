package nl.t64.game.rpg.components.character;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import nl.t64.game.rpg.constants.CharacterState;
import nl.t64.game.rpg.constants.Direction;
import nl.t64.game.rpg.events.Event;
import nl.t64.game.rpg.events.character.*;


public class InputNpc extends InputComponent {

    private static final float DEFAULT_STATE_TIME = 5f;

    private float stateTime = 0f;

    private CharacterState state;
    private Direction direction;
    private Direction originalDirection;

    @Override
    public void receive(Event event) {
        if (event instanceof StartStateEvent) {
            state = ((StartStateEvent) event).state;
        }
        if (event instanceof StartDirectionEvent) {
            direction = ((StartDirectionEvent) event).direction;
            originalDirection = direction;
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
        // empty
    }

    @Override
    public void update(Character npcCharacter, float dt) {
        stateTime -= dt;
        if (stateTime < 0) {
            setRandom();
        }
        npcCharacter.send(new StateEvent(state));
        npcCharacter.send(new DirectionEvent(direction));
    }

    private void setRandom() {
        stateTime = MathUtils.random(1.0f, 2.0f);

        switch (state) {
            case WALKING:
                state = CharacterState.IDLE;
                break;
            case IDLE:
                state = CharacterState.WALKING;
                direction = Direction.getRandom();
                break;
            case IMMOBILE:
                direction = originalDirection;
                break;
            default:
                throw new IllegalArgumentException(String.format("CharacterState '%s' not usable.", state));
        }
    }

    private void handleEvent(WaitEvent event) {
        Vector2 npcPosition = event.npcPosition;
        Vector2 playerPosition = event.playerPosition;
        stateTime = DEFAULT_STATE_TIME;
        if (state == CharacterState.WALKING) {
            state = CharacterState.IDLE;
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
