package nl.t64.game.rpg.screens.world;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import nl.t64.game.rpg.Utils;
import nl.t64.game.rpg.screens.world.entity.Direction;
import nl.t64.game.rpg.subjects.ActionObserver;


class GameMapWarpPoint extends GameMapRelocator implements ActionObserver {

    private GameMapWarpPoint(Rectangle rectangle,
                             String fromMapName,
                             String toMapName,
                             String toMapLocation,
                             Color fadeColor) {
        super(rectangle,
              fromMapName,
              toMapName,
              toMapLocation,
              fadeColor);
        Utils.getBrokerManager().actionObservers.addObserver(this);
    }

    static GameMapWarpPoint create(MapObject mapObject, String fromMapName) {
        RectangleMapObject rectObject = (RectangleMapObject) mapObject;
        return new GameMapWarpPoint(rectObject.getRectangle(),
                                    fromMapName,
                                    rectObject.getName(),
                                    createToMapLocation(rectObject),
                                    Color.WHITE);
    }

    @Override
    public void onNotifyActionPressed(Rectangle checkRect, Direction playerDirection, Vector2 playerPosition) {
        if (checkRect.overlaps(rectangle)) {
            Utils.getMapManager().checkWarpPoint(this, playerDirection);
        }
    }

}
