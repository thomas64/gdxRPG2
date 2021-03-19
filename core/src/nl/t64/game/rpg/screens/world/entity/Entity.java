package nl.t64.game.rpg.screens.world.entity;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import lombok.Getter;
import nl.t64.game.rpg.constants.Constant;
import nl.t64.game.rpg.screens.world.entity.events.Event;
import nl.t64.game.rpg.screens.world.entity.events.OnActionEvent;
import nl.t64.game.rpg.screens.world.entity.events.OnBumpEvent;
import nl.t64.game.rpg.subjects.ActionObserver;
import nl.t64.game.rpg.subjects.BlockObserver;
import nl.t64.game.rpg.subjects.BumpObserver;

import java.util.Optional;


public class Entity implements ActionObserver, BlockObserver, BumpObserver {

    @Getter
    private final String id;
    private final InputComponent inputComponent;
    private final PhysicsComponent physicsComponent;
    private final GraphicsComponent graphicsComponent;

    public Entity(String id, InputComponent input, PhysicsComponent physics, GraphicsComponent graphics) {
        this.id = id;
        this.inputComponent = input;
        this.physicsComponent = physics;
        this.graphicsComponent = graphics;
    }

    @Override
    public void onNotifyActionPressed(Rectangle checkRect, Direction playerDirection, Vector2 playerPosition) {
        send(new OnActionEvent(checkRect, playerDirection, playerPosition));
    }

    @Override
    public Optional<Rectangle> getBlockerFor(Rectangle boundingBox) {
        if (boundingBox.overlaps(physicsComponent.boundingBox)) {
            return Optional.of(physicsComponent.boundingBox);
        } else {
            return Optional.empty();
        }
    }

    @Override
    public boolean isBlocking(Vector2 point) {
        return physicsComponent.boundingBox.contains(point);
    }

    @Override
    public void onNotifyBump(Rectangle biggerBoundingBox, Rectangle checkRect, Vector2 playerPosition) {
        send(new OnBumpEvent(biggerBoundingBox, checkRect, playerPosition));
    }

    public void send(Event event) {
        inputComponent.receive(event);
        physicsComponent.receive(event);
        graphicsComponent.receive(event);
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
        inputComponent.dispose();
        physicsComponent.dispose();
        graphicsComponent.dispose();
    }

    public void resetInput() {
        inputComponent.reset();
    }

    public Vector2 getPositionInGrid() {
        return new Vector2((int) ((getPosition().x + (Constant.TILE_SIZE / 2f)) / (Constant.TILE_SIZE / 2f)),
                           (int) ((getPosition().y + (Constant.TILE_SIZE / 8f)) / (Constant.TILE_SIZE / 2f)));
    }

    public Vector2 getPosition() {
        return physicsComponent.currentPosition;
    }

    public Direction getDirection() {
        return physicsComponent.direction;
    }

    public EntityState getState() {
        return physicsComponent.state;
    }

    public String getConversationId() {
        return ((PhysicsNpc) physicsComponent).conversationId;
    }

}
