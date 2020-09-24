package nl.t64.game.rpg.screens.world;

import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import nl.t64.game.rpg.components.character.CharacterState;
import nl.t64.game.rpg.components.character.Direction;

import java.util.Optional;


class GameMapNpc {

    final Rectangle rectangle;
    final String name;
    final CharacterState state;
    final Direction direction;
    final String conversation;

    GameMapNpc(MapObject mapObject) {
        RectangleMapObject rectObject = (RectangleMapObject) mapObject;

        this.rectangle = rectObject.getRectangle();
        this.name = rectObject.getName();
        this.state = createState(rectObject);
        this.direction = createDirection(rectObject);
        this.conversation = createConversation(rectObject);
    }

    Vector2 getPosition() {
        return new Vector2(rectangle.x, rectangle.y);
    }

    private CharacterState createState(RectangleMapObject rectObject) {
        String characterState = rectObject.getProperties().get("type", String.class);
        if (characterState == null) {
            return CharacterState.IMMOBILE;
        } else if (characterState.equalsIgnoreCase("inv")) {
            return CharacterState.INVISIBLE;
        } else if (characterState.equalsIgnoreCase("w")) {
            return CharacterState.getRandom();
        } else if (characterState.equalsIgnoreCase("f")) {
            return CharacterState.FLOATING;
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

    private String createConversation(RectangleMapObject rectObject) {
        Optional<String> newConversation = Optional.ofNullable(
                rectObject.getProperties().get("conversation", String.class));
        return newConversation.orElse("default");
    }

}
