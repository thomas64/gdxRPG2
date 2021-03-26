package nl.t64.game.rpg.screens.world.mapobjects;

import com.badlogic.gdx.maps.objects.RectangleMapObject;
import lombok.Getter;
import nl.t64.game.rpg.screens.world.entity.Direction;


public class GameMapSpawnPoint extends GameMapObject {

    private final String fromMapName;
    private final String fromMapLocation;
    @Getter
    private final Direction direction;

    public GameMapSpawnPoint(RectangleMapObject rectObject) {
        super.rectangle = rectObject.getRectangle();
        this.fromMapName = rectObject.getName();
        this.fromMapLocation = createFromMapLocation(rectObject);
        this.direction = createDirection(rectObject);
    }

    public boolean isInConnectionWith(GameMapRelocator portal) {
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
        return rectObject.getProperties().get("type", "", String.class);
    }

    private Direction createDirection(RectangleMapObject rectObject) {
        return Direction.valueOf(rectObject.getProperties()
                                           .get("direction", "NONE", String.class)
                                           .toUpperCase());
    }

}
