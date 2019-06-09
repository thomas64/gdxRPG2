package nl.t64.game.rpg.components.character;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import nl.t64.game.rpg.constants.CharacterState;
import nl.t64.game.rpg.events.Event;

import java.util.List;


public class Character {

    private final List<Component> components;

    private final InputComponent inputComponent;
    private final PhysicsComponent physicsComponent;
    private final GraphicsComponent graphicsComponent;

    public Character(InputComponent input, PhysicsComponent physics, GraphicsComponent graphics) {
        this.inputComponent = input;
        this.physicsComponent = physics;
        this.graphicsComponent = graphics;

        this.components = List.of(this.inputComponent,
                                  this.physicsComponent,
                                  this.graphicsComponent);
    }

    public void send(Event event) {
        components.forEach(component -> component.receive(event));
    }

    public void update(float dt) {
        inputComponent.update(this, dt);
        physicsComponent.update(this, dt);
        graphicsComponent.update(dt);
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

    public void resetInput() {
        inputComponent.reset();
    }

    public Rectangle getBoundingBox() {
        return physicsComponent.boundingBox;
    }

    public Vector2 getPosition() {
        return physicsComponent.currentPosition;
    }

    public CharacterState getState() {
        return physicsComponent.state;
    }

}
