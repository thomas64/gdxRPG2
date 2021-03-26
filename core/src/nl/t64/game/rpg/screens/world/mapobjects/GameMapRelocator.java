package nl.t64.game.rpg.screens.world.mapobjects;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.math.Rectangle;
import lombok.Getter;
import nl.t64.game.rpg.screens.world.entity.Direction;

import java.util.Optional;


@Getter
public abstract class GameMapRelocator extends GameMapObject {

    private final String fromMapName;
    private final String toMapName;
    private final String toMapLocation;
    private final Color fadeColor;
    private Direction enterDirection;

    GameMapRelocator(Rectangle rectangle, String fromMapName, String toMapName, String toMapLocation, Color fadeColor) {
        this.rectangle = rectangle;
        this.fromMapName = fromMapName;
        this.toMapName = toMapName;
        this.toMapLocation = toMapLocation;
        this.fadeColor = fadeColor;
    }

    static String createToMapLocation(RectangleMapObject rectObject) {
        Optional<String> newToMapLocation = Optional.ofNullable(rectObject.getProperties().get("type", String.class));
        return newToMapLocation.orElse("");
    }

    public void setEnterDirection(Direction enterDirection) {
        this.enterDirection = enterDirection;
    }

}
