package nl.t64.game.rpg.entities;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.utils.Array;
import nl.t64.game.rpg.Camera;
import nl.t64.game.rpg.MapManager;
import nl.t64.game.rpg.components.Component;
import nl.t64.game.rpg.components.GraphicsComponent;
import nl.t64.game.rpg.components.InputComponent;
import nl.t64.game.rpg.components.PhysicsComponent;
import nl.t64.game.rpg.events.Event;


public class Entity {

    private static final String TAG = Entity.class.getSimpleName();

    private static final int MAX_COMPONENTS = 5;
    private Array<Component> components;

    private InputComponent inputComponent;
    private PhysicsComponent physicsComponent;
    private GraphicsComponent graphicsComponent;

    public Entity(InputComponent ic, PhysicsComponent pc, GraphicsComponent gc) {
        inputComponent = ic;
        physicsComponent = pc;
        graphicsComponent = gc;

        components = new Array<>(MAX_COMPONENTS);
        components.add(inputComponent);
        components.add(physicsComponent);
        components.add(graphicsComponent);
    }

    public void send(Event event) {
        for (Component component : components) {
            component.receive(event);
        }
    }

    public void update(MapManager mapManager, Camera camera, float dt) {
        inputComponent.update(this, dt);
        physicsComponent.update(this, mapManager, camera, dt);
        graphicsComponent.update();
    }

    public void render(Batch batch) {
        graphicsComponent.render(batch);
    }

    public void dispose() {
        for (Component component : components) {
            component.dispose();
        }
    }

}
