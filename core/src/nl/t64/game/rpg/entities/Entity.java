package nl.t64.game.rpg.entities;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import lombok.Getter;
import lombok.Setter;
import nl.t64.game.rpg.components.GraphicsComponent;
import nl.t64.game.rpg.components.InputComponent;
import nl.t64.game.rpg.constants.Constant;
import nl.t64.game.rpg.constants.Direction;
import nl.t64.game.rpg.constants.EntityState;

import java.util.UUID;


public class Entity {

    private static final String TAG = Entity.class.getSimpleName();
    private static final float BOUNDING_BOX_PERCENTAGE = 0.75f;

    @Getter
    private Rectangle boundingBox;

    private Vector2 oldPosition;
    @Getter
    private Vector2 currentPosition;
    @Getter
    @Setter
    private Direction direction = null;

    private Vector2 velocity;

    private String entityId;

    @Getter
    @Setter
    private EntityState state = EntityState.IDLE;

    @Getter
    private InputComponent inputComponent;
    private GraphicsComponent graphicsComponent;

    public Entity() {
        this.entityId = UUID.randomUUID().toString();
        this.boundingBox = new Rectangle();
        this.oldPosition = new Vector2();
        this.currentPosition = new Vector2();
        this.velocity = new Vector2(192f, 192f);  // 48 * 4

        inputComponent = new InputComponent();
        graphicsComponent = new GraphicsComponent();
    }

    public void update(float dt) {
        inputComponent.update(this, dt);
        graphicsComponent.update(this);
        setBoundingBox();
    }

    public void render(Batch batch) {
        graphicsComponent.render(batch, this);
    }

    private void setBoundingBox() {
        float width = Constant.TILE_SIZE * BOUNDING_BOX_PERCENTAGE;
        float height = Constant.TILE_SIZE * BOUNDING_BOX_PERCENTAGE;
        float widthReduction = 1.00f - BOUNDING_BOX_PERCENTAGE;
        float x = currentPosition.x + (Constant.TILE_SIZE * (widthReduction / 2));
        float y = currentPosition.y;
        boundingBox.set(x, y, width, height);
    }

    public void init(Vector2 spawnPosition, Direction spawnDirection) {
        setOldPosition(spawnPosition.x, spawnPosition.y);
        setCurrentPosition(spawnPosition.x, spawnPosition.y);
        direction = spawnDirection;
    }

    public void move(float dt) {
        oldPosition = currentPosition.cpy();

        switch (direction) {
            case NORTH:
                currentPosition.y += velocity.y * dt;
                break;
            case SOUTH:
                currentPosition.y -= velocity.y * dt;
                break;
            case WEST:
                currentPosition.x -= velocity.x * dt;
                break;
            case EAST:
                currentPosition.x += velocity.x * dt;
                break;
        }
    }

    public void moveBack() {
        switch (direction) {
            case NORTH:
                currentPosition.y -= 1;
                break;
            case SOUTH:
                currentPosition.y += 1;
                break;
            case WEST:
                currentPosition.x += 1;
                break;
            case EAST:
                currentPosition.x -= 1;
                break;
        }
        float roundedX = Math.round(currentPosition.x);
        float roundedY = Math.round(currentPosition.y);
        setCurrentPosition(roundedX, roundedY);
        setBoundingBox();
    }

    public void alignToGrid() {
        float roundedX = Math.round(currentPosition.x / Constant.TILE_SIZE) * Constant.TILE_SIZE;
        float roundedY = Math.round(currentPosition.y / Constant.TILE_SIZE) * Constant.TILE_SIZE;
        setCurrentPosition(roundedX, roundedY);
    }

    public void dispose() {
        inputComponent.dispose();
        graphicsComponent.dispose();
    }

    private void setOldPosition(float positionX, float positionY) {
        oldPosition.x = positionX;
        oldPosition.y = positionY;
    }

    private void setCurrentPosition(float positionX, float positionY) {
        currentPosition.x = positionX;
        currentPosition.y = positionY;
    }

}
