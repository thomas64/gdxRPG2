package nl.t64.game.rpg.components.character;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import lombok.Getter;
import nl.t64.game.rpg.constants.Constant;
import nl.t64.game.rpg.events.Event;

import java.util.List;


public class Character {

    @Getter
    private final String id;
    private final List<Component> components;
    private final InputComponent inputComponent;
    private final PhysicsComponent physicsComponent;
    private final GraphicsComponent graphicsComponent;

    public Character(String id, InputComponent input, PhysicsComponent physics, GraphicsComponent graphics) {
        this.id = id;
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

    public void registerObserver(ComponentObserver observer) {
        inputComponent.addObserver(observer);
        physicsComponent.addObserver(observer);
        graphicsComponent.addObserver(observer);
    }

    public void unregisterObserver() {
        inputComponent.removeAllObservers();
        physicsComponent.removeAllObservers();
        graphicsComponent.removeAllObservers();
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

    public Vector2 getPositionInGrid() {
        return new Vector2((int) ((getPosition().x + (Constant.TILE_SIZE / 2f)) / (Constant.TILE_SIZE / 2f)),
                           (int) ((getPosition().y + (Constant.TILE_SIZE / 8f)) / (Constant.TILE_SIZE / 2f)));
    }

    public Vector2 getPosition() {
        return physicsComponent.currentPosition;
    }

    public Rectangle getRectangle() {
        Vector2 position = physicsComponent.currentPosition;
        return new Rectangle(position.x, position.y, Constant.TILE_SIZE, Constant.TILE_SIZE);
    }

    public Direction getDirection() {
        return physicsComponent.direction;
    }

    public CharacterState getState() {
        return physicsComponent.state;
    }

    public String getConversationId() {
        return ((PhysicsNpc) physicsComponent).conversationId;
    }

}
