package nl.t64.game.rpg.tiled;

import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.math.Rectangle;
import lombok.Getter;
import lombok.Setter;
import nl.t64.game.rpg.constants.Direction;

import java.util.Optional;


@Getter
public class Portal {

    private final Rectangle rectangle;
    private final String fromMapName;
    private final String toMapName;
    private final String toMapLocation;
    @Setter
    private Direction enterDirection;

    Portal(MapObject mapObject, String fromMapName) {
        RectangleMapObject rectObject = (RectangleMapObject) mapObject;

        this.rectangle = rectObject.getRectangle();
        this.fromMapName = fromMapName;
        this.toMapName = rectObject.getName();
        this.toMapLocation = createToMapLocation(rectObject);
    }

    private String createToMapLocation(RectangleMapObject rectObject) {
        Optional<String> newToMapLocation = Optional.ofNullable(
                rectObject.getProperties().get("type", String.class));
        return newToMapLocation.orElse("");

    }

}
