package nl.t64.game.rpg.tiled;

import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import lombok.Getter;
import nl.t64.game.rpg.constants.CharacterState;
import nl.t64.game.rpg.constants.Direction;

import java.util.Optional;


@Getter
public class Npc {

    private final Rectangle rectangle;
    private final String name;
    private final CharacterState state;
    private final Direction direction;

    public Npc(MapObject mapObject) {
        RectangleMapObject rectObject = (RectangleMapObject) mapObject;

        this.rectangle = rectObject.getRectangle();
        this.name = rectObject.getName();
        this.state = createState(rectObject);
        this.direction = createDirection(rectObject);
    }

    public Vector2 getPosition() {
        return new Vector2(rectangle.x, rectangle.y);
    }

    private CharacterState createState(RectangleMapObject rectObject) {
        String characterState = rectObject.getProperties().get("type", String.class);
        if (characterState == null) {
            return CharacterState.IMMOBILE;
        } else if (characterState.equalsIgnoreCase("w")) {
            return CharacterState.getRandom();
        } else {
            throw new IllegalArgumentException(String.format("CharacterState '%s' unknown.", characterState));
        }
    }

    private Direction createDirection(RectangleMapObject rectObject) {
        Optional<String> newDirection = Optional.ofNullable(
                rectObject.getProperties().get("direction", String.class));
        return newDirection.map(string -> Direction.valueOf(string.toUpperCase()))
                           .orElseGet(Direction::getRandom);
    }

}
