package nl.t64.game.rpg.components;

import com.badlogic.gdx.math.MathUtils;
import nl.t64.game.rpg.constants.Direction;
import nl.t64.game.rpg.constants.EntityState;
import nl.t64.game.rpg.entities.Entity;
import nl.t64.game.rpg.events.*;


public class NpcInputComponent extends InputComponent {

    private static final String TAG = NpcInputComponent.class.getSimpleName();

    private float stateTime = 0f;

    private EntityState state;
    private Direction direction;

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
    }

    @Override
    public void dispose() {
    }

    @Override
    public void update(Entity entity, float dt) {
        stateTime -= dt;
        if (stateTime < 0) {
            setRandom(entity);
        }
    }

    private void setRandom(Entity entity) {
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

        entity.send(new StateEvent(state));
        entity.send(new DirectionEvent(direction));
    }

}
