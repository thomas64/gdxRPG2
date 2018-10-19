package nl.t64.game.rpg.components.character;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import nl.t64.game.rpg.constants.CharacterState;
import nl.t64.game.rpg.constants.Direction;
import nl.t64.game.rpg.entities.Character;
import nl.t64.game.rpg.events.Event;
import nl.t64.game.rpg.events.character.*;


public class NpcInputComponent extends InputComponent {

    private static final String TAG = NpcInputComponent.class.getSimpleName();

    private float stateTime = 0f;

    private CharacterState state;
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
    public void update(Character npcCharacter, float dt) {
        stateTime -= dt;
        if (stateTime < 0) {
            if (stateWasImmobile) {
                restoreOriginal();
            } else {
                setRandom();
            }
        }
        npcCharacter.send(new StateEvent(state));
        npcCharacter.send(new DirectionEvent(direction));
    }

    private void restoreOriginal() {
        direction = originalDirection;
        stateWasImmobile = false;
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
                // keep on being immobile.
                break;
            default:
                throw new IllegalArgumentException(String.format("CharacterState '%s' not usable.", state));
        }
    }

    private void handleEvent(WaitEvent event) {
        Vector2 npcPosition = event.getNpcPosition();
        Vector2 playerPosition = event.getPlayerPosition();
        stateTime = 5f;
        if (state == CharacterState.IMMOBILE && !stateWasImmobile) {
            stateWasImmobile = true;
            originalDirection = direction;
        } else if (state == CharacterState.WALKING) {
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
