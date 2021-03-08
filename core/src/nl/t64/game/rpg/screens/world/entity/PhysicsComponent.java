package nl.t64.game.rpg.screens.world.entity;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import nl.t64.game.rpg.constants.Constant;


abstract class PhysicsComponent implements Component {

    public final ComponentSubject componentSubject;
    EntityState state;
    Direction direction;
    float velocity;
    Rectangle boundingBox;
    Vector2 oldPosition;
    Vector2 currentPosition;
    float boundingBoxWidthPercentage;
    float boundingBoxHeightPercentage;

    PhysicsComponent() {
        this.componentSubject = new ComponentSubject();
        this.direction = null;
        this.boundingBox = new Rectangle();
        this.oldPosition = new Vector2();
        this.currentPosition = new Vector2();
    }

    public abstract void update(Entity entity, float dt);

    public abstract void debug(ShapeRenderer shapeRenderer);

    void setBoundingBox() {
        float width = Constant.TILE_SIZE * boundingBoxWidthPercentage;
        float height = Constant.TILE_SIZE * boundingBoxHeightPercentage;
        float widthReduction = 1.00f - boundingBoxWidthPercentage;
        float x = currentPosition.x + (Constant.TILE_SIZE * (widthReduction / 2));
        float y = currentPosition.y;
        boundingBox.set(x, y, width, height);
    }

    void move(float dt) {
        oldPosition.set(Math.round(currentPosition.x), Math.round(currentPosition.y));

        switch (direction) {
            case NORTH -> currentPosition.y += velocity * dt;
            case SOUTH -> currentPosition.y -= velocity * dt;
            case WEST -> currentPosition.x -= velocity * dt;
            case EAST -> currentPosition.x += velocity * dt;
            case NONE -> throw new IllegalArgumentException("Direction 'NONE' is not usable.");
        }
        setRoundPosition();
    }

    void moveBack() {
        switch (direction) {
            case NORTH -> currentPosition.y -= 1;
            case SOUTH -> currentPosition.y += 1;
            case WEST -> currentPosition.x += 1;
            case EAST -> currentPosition.x -= 1;
            case NONE -> throw new IllegalArgumentException("Direction 'NONE' is not usable.");
        }
        setRoundPosition();
    }

    void setRoundPosition() {
        currentPosition.set(Math.round(currentPosition.x), Math.round(currentPosition.y));
        setBoundingBox();
    }

}
