package nl.t64.game.rpg.components;

import com.badlogic.gdx.math.MathUtils;
import nl.t64.game.rpg.constants.Direction;
import nl.t64.game.rpg.constants.EntityState;
import nl.t64.game.rpg.entities.Entity;
import nl.t64.game.rpg.events.CollisionEvent;
import nl.t64.game.rpg.events.DirectionEvent;
import nl.t64.game.rpg.events.Event;
import nl.t64.game.rpg.events.StateEvent;


public class NpcInputComponent extends InputComponent {

    private static final String TAG = NpcInputComponent.class.getSimpleName();

    private float stateTime = 0f;

    private EntityState state;
    private Direction direction;

    public NpcInputComponent() {
        state = EntityState.WALKING;
        direction = Direction.getRandom();
    }

    @Override
    public void receive(Event event) {
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

        if (state == EntityState.WALKING) {
            state = EntityState.IDLE;
        } else if (state == EntityState.IDLE) {
            state = EntityState.WALKING;
            direction = Direction.getRandom();
        }
        entity.send(new DirectionEvent(direction));
        entity.send(new StateEvent(state));
    }

}
