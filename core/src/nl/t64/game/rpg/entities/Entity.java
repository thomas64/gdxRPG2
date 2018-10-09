package nl.t64.game.rpg.entities;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import nl.t64.game.rpg.MapManager;
import nl.t64.game.rpg.components.Component;
import nl.t64.game.rpg.components.GraphicsComponent;
import nl.t64.game.rpg.components.InputComponent;
import nl.t64.game.rpg.components.PhysicsComponent;
import nl.t64.game.rpg.constants.EntityState;
import nl.t64.game.rpg.events.Event;

import java.util.ArrayList;
import java.util.List;


public class Entity {

    private static final String TAG = Entity.class.getSimpleName();

    private static final int MAX_COMPONENTS = 5;
    private List<Component> components;

    private InputComponent inputComponent;
    private PhysicsComponent physicsComponent;
    private GraphicsComponent graphicsComponent;

    public Entity(InputComponent ic, PhysicsComponent pc, GraphicsComponent gc) {
        inputComponent = ic;
        physicsComponent = pc;
        graphicsComponent = gc;

        components = new ArrayList<>(MAX_COMPONENTS);
        components.add(inputComponent);
        components.add(physicsComponent);
        components.add(graphicsComponent);
    }

    public void send(Event event) {
        components.forEach(component -> component.receive(event));
    }

    public void update(MapManager mapManager, List<Entity> npcEntities, float dt) {
        inputComponent.update(this, dt);
        physicsComponent.update(this, mapManager, npcEntities, dt);
        graphicsComponent.update();
    }

    public void render(Batch batch, ShapeRenderer shapeRenderer) {
        graphicsComponent.render(this, batch, shapeRenderer);
    }

    public void debug(ShapeRenderer shapeRenderer) {
        physicsComponent.debug(shapeRenderer);
    }

    public void dispose() {
        components.forEach(Component::dispose);
    }

    public Rectangle getBoundingBox() {
        return physicsComponent.getBoundingBox();
    }

    public Vector2 getPosition() {
        return physicsComponent.getCurrentPosition();
    }

    public EntityState getState() {
        return physicsComponent.getState();
    }

}
