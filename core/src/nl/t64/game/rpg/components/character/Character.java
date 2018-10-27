package nl.t64.game.rpg.components.character;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import nl.t64.game.rpg.MapManager;
import nl.t64.game.rpg.constants.CharacterState;
import nl.t64.game.rpg.events.Event;

import java.util.ArrayList;
import java.util.List;


public class Character {

    private static final String TAG = Character.class.getSimpleName();

    private static final int MAX_COMPONENTS = 3;
    private List<Component> components;

    private Input inputComponent;
    private Physics physicsComponent;
    private Graphics graphicsComponent;

    public Character(Input input, Physics physics, Graphics graphics) {
        inputComponent = input;
        physicsComponent = physics;
        graphicsComponent = graphics;

        components = new ArrayList<>(MAX_COMPONENTS);
        components.add(inputComponent);
        components.add(physicsComponent);
        components.add(graphicsComponent);
    }

    public void send(Event event) {
        components.forEach(component -> component.receive(event));
    }

    public void update(MapManager mapManager, List<Character> npcCharacters, float dt) {
        inputComponent.update(this, dt);
        physicsComponent.update(this, mapManager, npcCharacters, dt);
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

    public void updateInput(float dt) {
        inputComponent.update(this, dt);
    }

    public Rectangle getBoundingBox() {
        return physicsComponent.getBoundingBox();
    }

    public Vector2 getPosition() {
        return physicsComponent.getCurrentPosition();
    }

    public CharacterState getState() {
        return physicsComponent.getState();
    }

}
