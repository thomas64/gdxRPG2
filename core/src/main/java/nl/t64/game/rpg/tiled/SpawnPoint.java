package nl.t64.game.rpg.tiled;

import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.math.Rectangle;
import lombok.Getter;
import nl.t64.game.rpg.constants.Direction;
import nl.t64.game.rpg.constants.MapTitle;

import java.util.Optional;


public class SpawnPoint {

    @Getter
    private final Rectangle rectangle;
    private final MapTitle fromMapName;
    private final String fromMapLocation;
    @Getter
    private final Direction direction;

    public SpawnPoint(MapObject mapObject) {
        RectangleMapObject rectObject = (RectangleMapObject) mapObject;

        this.rectangle = rectObject.getRectangle();
        this.fromMapName = MapTitle.valueOf(rectObject.getName().toUpperCase());
        this.fromMapLocation = createFromMapLocation(rectObject);
        this.direction = createDirection(rectObject);
    }

    public boolean isInConnectionWith(Portal portal) {
        return (fromMapName.equals(portal.getFromMapName()) &&
                fromMapLocation.equalsIgnoreCase(portal.getToMapLocation()));
    }

    public float getX() {
        return rectangle.getX();
    }

    public float getY() {
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
