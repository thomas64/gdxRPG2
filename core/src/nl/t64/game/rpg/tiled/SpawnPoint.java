package nl.t64.game.rpg.tiled;

import com.badlogic.gdx.math.Rectangle;
import lombok.Getter;


public class SpawnPoint {

    @Getter
    private final Rectangle rectangle;
    private final String fromMapName;
    private final String fromMapLocation;
    @Getter
    private final String direction;

    public SpawnPoint(Rectangle rectangle, String fromMapName, String fromMapLocation, String direction) {
        this.rectangle = rectangle;
        this.fromMapName = fromMapName;
        if (fromMapLocation == null) fromMapLocation = "";
        this.fromMapLocation = fromMapLocation;
        if (direction == null) direction = "";
        this.direction = direction;
    }

    public boolean isInConnectionWith(Portal portal) {
        return (fromMapName.equalsIgnoreCase(portal.getFromMapName()) &&
                fromMapLocation.equalsIgnoreCase(portal.getToMapLocation()));
    }

    public float getX() {
        return rectangle.getX();
    }

    public float getY() {
        return rectangle.getY();
    }

}
