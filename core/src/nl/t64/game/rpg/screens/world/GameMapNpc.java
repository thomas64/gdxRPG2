package nl.t64.game.rpg.screens.world;

import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.math.Vector2;
import nl.t64.game.rpg.screens.world.entity.Direction;
import nl.t64.game.rpg.screens.world.entity.EntityState;

import java.util.Optional;


class GameMapNpc extends GameMapObject {

    final String name;
    final EntityState state;
    final Direction direction;
    final String conversation;

    GameMapNpc(RectangleMapObject rectObject) {
        super.rectangle = rectObject.getRectangle();
        this.name = rectObject.getName();
        this.state = createState(rectObject);
        this.direction = createDirection(rectObject);
        this.conversation = createConversation(rectObject);
    }

    Vector2 getPosition() {
        return new Vector2(rectangle.x, rectangle.y);
    }

    private EntityState createState(RectangleMapObject rectObject) {
        String entityState = rectObject.getProperties().get("type", String.class);
        if (entityState == null) {
            return EntityState.IMMOBILE;
        } else if (entityState.equalsIgnoreCase("inv")) {
            return EntityState.INVISIBLE;
        } else if (entityState.equalsIgnoreCase("w")) {
            return EntityState.getRandom();
        } else if (entityState.equalsIgnoreCase("f")) {
            return EntityState.FLOATING;
        } else {
            throw new IllegalArgumentException(String.format("EntityState '%s' unknown.", entityState));
        }
    }

    private Direction createDirection(RectangleMapObject rectObject) {
        return Optional.ofNullable(rectObject.getProperties().get("direction", String.class))
                       .map(directionString -> Direction.valueOf(directionString.toUpperCase()))
                       .orElseGet(Direction::getRandom);
    }

    private String createConversation(RectangleMapObject rectObject) {
        return rectObject.getProperties().get("conversation", "default", String.class);
    }

}
