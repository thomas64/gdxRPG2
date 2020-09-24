package nl.t64.game.rpg.screens.world;

import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.math.Rectangle;
import nl.t64.game.rpg.components.character.Direction;

import java.util.Optional;


class GameMapPortal {

    final Rectangle rectangle;
    final String fromMapName;
    final String toMapName;
    final String toMapLocation;
    Direction enterDirection;

    GameMapPortal(MapObject mapObject, String fromMapName) {
        RectangleMapObject rectObject = (RectangleMapObject) mapObject;

        this.rectangle = rectObject.getRectangle();
        this.fromMapName = fromMapName;
        this.toMapName = rectObject.getName();
        this.toMapLocation = createToMapLocation(rectObject);
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
