package nl.t64.game.rpg.components.character;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import nl.t64.game.rpg.constants.CharacterState;
import nl.t64.game.rpg.constants.Constant;
import nl.t64.game.rpg.constants.Direction;


abstract class PhysicsComponent implements Component {

    CharacterState state;
    Direction direction;
    float velocity;
    Rectangle boundingBox;
    Vector2 oldPosition;
    Vector2 currentPosition;
    float boundingBoxWidthPercentage;
    float boundingBoxHeightPercentage;

    PhysicsComponent() {
        this.direction = null;
        this.boundingBox = new Rectangle();
        this.oldPosition = new Vector2();
        this.currentPosition = new Vector2();
    }

    public abstract void update(Character character, float dt);

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
        oldPosition.x = Math.round(currentPosition.cpy().x);
        oldPosition.y = Math.round(currentPosition.cpy().y);

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
