package nl.t64.game.rpg.components.character;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import lombok.Getter;
import nl.t64.game.rpg.MapManager;
import nl.t64.game.rpg.components.Component;
import nl.t64.game.rpg.constants.CharacterState;
import nl.t64.game.rpg.constants.Constant;
import nl.t64.game.rpg.constants.Direction;
import nl.t64.game.rpg.entities.Character;

import java.util.List;


public abstract class PhysicsComponent implements Component {

    @Getter
    CharacterState state;
    Direction direction = null;
    float velocity;
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

    public abstract void update(Character character, MapManager mapManager, List<Character> npcCharacters, float dt);

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
            case NORTH:
                currentPosition.y += velocity * dt;
                break;
            case SOUTH:
                currentPosition.y -= velocity * dt;
                break;
            case WEST:
                currentPosition.x -= velocity * dt;
                break;
            case EAST:
                currentPosition.x += velocity * dt;
                break;
            default:
                throw new IllegalArgumentException(String.format("Direction '%s' not usable.", direction));
        }
        setRoundPosition();
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
        setRoundPosition();
    }

    void setRoundPosition() {
        currentPosition.set(Math.round(currentPosition.x), Math.round(currentPosition.y));
        setBoundingBox();
    }

}
