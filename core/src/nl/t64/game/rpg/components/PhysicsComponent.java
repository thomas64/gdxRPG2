package nl.t64.game.rpg.components;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import lombok.Getter;
import nl.t64.game.rpg.MapManager;
import nl.t64.game.rpg.constants.Constant;
import nl.t64.game.rpg.constants.Direction;
import nl.t64.game.rpg.constants.EntityState;
import nl.t64.game.rpg.entities.Entity;


public abstract class PhysicsComponent implements Component {

    private static final String TAG = PhysicsComponent.class.getSimpleName();

    EntityState state;
    Direction direction = null;
    Vector2 velocity;
    Vector2 oldPosition;
    @Getter
    Vector2 currentPosition;
    @Getter
    Rectangle boundingBox;
    float boundingBoxWidthPercentage;
    float boundingBoxHeightPercentage;


    PhysicsComponent() {
        this.boundingBox = new Rectangle();
        this.oldPosition = new Vector2();
        this.currentPosition = new Vector2();
    }

    public abstract void update(Entity entity, MapManager mapManager, Array<Entity> entities, float dt);

    void setBoundingBox() {
        float width = Constant.TILE_SIZE * boundingBoxWidthPercentage;
        float height = Constant.TILE_SIZE * boundingBoxHeightPercentage;
        float widthReduction = 1.00f - boundingBoxWidthPercentage;
        float x = currentPosition.x + (Constant.TILE_SIZE * (widthReduction / 2));
        float y = currentPosition.y;
        boundingBox.set(x, y, width, height);
    }

    void move(float dt) {
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
            default:
                throw new IllegalArgumentException(String.format("Direction '%s' not usable.", direction));
        }
    }

    void moveBack() {
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
            default:
                throw new IllegalArgumentException(String.format("Direction '%s' not usable.", direction));
        }

        float roundedX = Math.round(currentPosition.x);
        float roundedY = Math.round(currentPosition.y);
        setCurrentPosition(roundedX, roundedY);
        setBoundingBox();
    }

    void setCurrentPosition(float positionX, float positionY) {
        currentPosition.x = positionX;
        currentPosition.y = positionY;
    }

}
