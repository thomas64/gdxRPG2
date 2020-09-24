package nl.t64.game.rpg.screens.world;

import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.math.Rectangle;
import nl.t64.game.rpg.components.character.Direction;

import java.util.Optional;


class GameMapSpawnPoint {

    final Rectangle rectangle;
    private final String fromMapName;
    private final String fromMapLocation;
    final Direction direction;

    GameMapSpawnPoint(MapObject mapObject) {
        RectangleMapObject rectObject = (RectangleMapObject) mapObject;

        this.rectangle = rectObject.getRectangle();
        this.fromMapName = rectObject.getName();
        this.fromMapLocation = createFromMapLocation(rectObject);
        this.direction = createDirection(rectObject);
    }

    boolean isInConnectionWith(GameMapPortal portal) {
        return (fromMapName.equals(portal.fromMapName) &&
                fromMapLocation.equalsIgnoreCase(portal.toMapLocation));
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
