package nl.t64.game.rpg.screens.world;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.math.Rectangle;
import nl.t64.game.rpg.screens.world.entity.Direction;

import java.util.Optional;


abstract class GameMapRelocator extends GameMapObject {

    final String fromMapName;
    final String toMapName;
    final String toMapLocation;
    final Color fadeColor;
    Direction enterDirection;

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

    void setEnterDirection(Direction enterDirection) {
        this.enterDirection = enterDirection;
    }

}
