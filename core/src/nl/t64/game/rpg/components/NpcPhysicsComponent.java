package nl.t64.game.rpg.components;

import com.badlogic.gdx.math.Vector2;
import nl.t64.game.rpg.Camera;
import nl.t64.game.rpg.MapManager;
import nl.t64.game.rpg.constants.EntityState;
import nl.t64.game.rpg.entities.Entity;
import nl.t64.game.rpg.events.*;


public class NpcPhysicsComponent extends PhysicsComponent {

    private static final String TAG = NpcPhysicsComponent.class.getSimpleName();

    public NpcPhysicsComponent() {
        this.velocity = new Vector2(48f, 48f);
    }

    @Override
    public void receive(Event event) {
        if (event instanceof StartStateEvent) {
            state = ((StartStateEvent) event).getState();
        }
        if (event instanceof StateEvent) {
            state = ((StateEvent) event).getState();
        }
        if (event instanceof StartPositionEvent) {
            currentPosition = ((StartPositionEvent) event).getPosition();
        }
        if (event instanceof StartDirectionEvent) {
            direction = ((StartDirectionEvent) event).getDirection();
        }
        if (event instanceof DirectionEvent) {
            direction = ((DirectionEvent) event).getDirection();
        }
    }

    @Override
    public void dispose() {
    }

    @Override
    public void update(Entity entity, MapManager mapManager, Camera camera, float dt) {
        relocate(dt);
        setBoundingBox();
        checkBlocker(entity, mapManager);
        entity.send(new PositionEvent(currentPosition));
    }

    private void relocate(float dt) {
        if (state == EntityState.WALKING) {
            move(dt);
        }
    }


}
