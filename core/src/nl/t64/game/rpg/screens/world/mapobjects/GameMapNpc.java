package nl.t64.game.rpg.screens.world.mapobjects;

import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.math.Vector2;
import lombok.Getter;
import nl.t64.game.rpg.screens.world.entity.Direction;
import nl.t64.game.rpg.screens.world.entity.EntityState;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;


@Getter
public class GameMapNpc extends GameMapObject {

    private final String name;
    private final EntityState state;
    private final Direction direction;
    private final String conversation;
    private final List<String> conditionIds;

    public GameMapNpc(RectangleMapObject rectObject) {
        super.rectangle = rectObject.getRectangle();
        this.name = rectObject.getName();
        this.state = createState(rectObject);
        this.direction = createDirection(rectObject);
        this.conversation = createConversation(rectObject);
        this.conditionIds = createConditions(rectObject);
    }

    public Vector2 getPosition() {
        return new Vector2(rectangle.x, rectangle.y);
    }

    private EntityState createState(RectangleMapObject rectObject) {
        String entityState = rectObject.getProperties().get("type", String.class);
        if (entityState == null) {
            return EntityState.IMMOBILE;
        } else if (entityState.equalsIgnoreCase("inv")) {
            return EntityState.INVISIBLE;
        } else if (entityState.equalsIgnoreCase("w")) {
            return EntityState.getRandomIdleOrWalking();
        } else if (entityState.equalsIgnoreCase("ia")) {
            return EntityState.IDLE_ANIMATING;
        } else if (entityState.equalsIgnoreCase("f")) {
            return EntityState.FLYING;
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

    private List<String> createConditions(RectangleMapObject rectObject) {
        return Optional.ofNullable(rectObject.getProperties().get("condition", String.class))
                       .map(condition -> condition.split("\\s*,\\s*"))
                       .map(Arrays::asList)
                       .orElseGet(List::of);
    }

}
