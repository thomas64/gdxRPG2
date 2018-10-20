package nl.t64.game.rpg.tiled;

import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.math.Rectangle;
import lombok.Getter;
import lombok.Setter;
import nl.t64.game.rpg.constants.Direction;
import nl.t64.game.rpg.constants.MapTitle;


@Getter
public class Portal {

    private final Rectangle rectangle;
    private final MapTitle fromMapName;
    private final MapTitle toMapName;
    private final String toMapLocation;
    @Setter
    private Direction enterDirection;

    public Portal(MapObject mapObject, MapTitle fromMapName) {
        RectangleMapObject rectObject = (RectangleMapObject) mapObject;

        this.rectangle = rectObject.getRectangle();
        this.fromMapName = fromMapName;
        this.toMapName = MapTitle.valueOf(rectObject.getName().toUpperCase());
        this.toMapLocation = createToMapLocation(rectObject);
    }

    private String createToMapLocation(RectangleMapObject rectObject) {
        String newToMapLocation = rectObject.getProperties().get("type", String.class);
        if (newToMapLocation == null) {
            newToMapLocation = "";
        }
        return newToMapLocation;
    }

}
