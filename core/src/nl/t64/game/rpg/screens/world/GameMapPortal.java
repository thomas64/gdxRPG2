package nl.t64.game.rpg.screens.world;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import nl.t64.game.rpg.screens.world.entity.Direction;

import java.util.Optional;


class GameMapPortal extends GameMapObject {

    final String fromMapName;
    final String toMapName;
    final String toMapLocation;
    final Color fadeColor;
    Direction enterDirection;

    GameMapPortal(MapObject mapObject, String fromMapName) {
        this(mapObject, fromMapName, Color.BLACK);
    }

    GameMapPortal(MapObject mapObject, String fromMapName, Color fadeColor) {
        RectangleMapObject rectObject = (RectangleMapObject) mapObject;

        this.rectangle = rectObject.getRectangle();
        this.fromMapName = fromMapName;
        this.toMapName = rectObject.getName();
        this.toMapLocation = createToMapLocation(rectObject);
        this.fadeColor = fadeColor;
    }

    void setEnterDirection(Direction enterDirection) {
        this.enterDirection = enterDirection;
    }

    private String createToMapLocation(RectangleMapObject rectObject) {
        Optional<String> newToMapLocation = Optional.ofNullable(
                rectObject.getProperties().get("type", String.class));
        return newToMapLocation.orElse("");
    }

}
