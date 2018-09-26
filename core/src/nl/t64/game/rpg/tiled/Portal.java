package nl.t64.game.rpg.tiled;

import com.badlogic.gdx.math.Rectangle;
import lombok.Getter;


@Getter
public class Portal {

    private final Rectangle rectangle;
    private final String fromMapName;
    private final String toMapName;
    private final String toMapLocation;

    public Portal(Rectangle rectangle, String fromMapName, String toMapName, String toMapLocation) {
        this.rectangle = rectangle;
        this.fromMapName = fromMapName;
        this.toMapName = toMapName;
        if (toMapLocation == null) toMapLocation = "";
        this.toMapLocation = toMapLocation;
    }
}
