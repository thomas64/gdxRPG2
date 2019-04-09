package nl.t64.game.rpg.tiled;

import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.math.Rectangle;
import lombok.Getter;
import nl.t64.game.rpg.constants.Direction;

import java.util.Optional;


class SpawnPoint {

    @Getter
    private final Rectangle rectangle;
    private final String fromMapName;
    private final String fromMapLocation;
    @Getter
    private final Direction direction;

    SpawnPoint(MapObject mapObject) {
        RectangleMapObject rectObject = (RectangleMapObject) mapObject;

        this.rectangle = rectObject.getRectangle();
        this.fromMapName = rectObject.getName();
        this.fromMapLocation = createFromMapLocation(rectObject);
        this.direction = createDirection(rectObject);
    }

    boolean isInConnectionWith(Portal portal) {
        return (fromMapName.equals(portal.getFromMapName()) &&
                fromMapLocation.equalsIgnoreCase(portal.getToMapLocation()));
    }

    float getX() {
        return rectangle.getX();
    }

    float getY() {
        return rectangle.getY();
    }

    private String createFromMapLocation(RectangleMapObject rectObject) {
        Optional<String> newFromMapLocation = Optional.ofNullable(
                rectObject.getProperties().get("type", String.class));
        return newFromMapLocation.orElse("");
    }

    private Direction createDirection(RectangleMapObject rectObject) {
        Optional<String> directionString = Optional.ofNullable(
                rectObject.getProperties().get("direction", String.class));
        return directionString.map(string -> Direction.valueOf(string.toUpperCase()))
                              .orElse(Direction.NONE);
    }

}
