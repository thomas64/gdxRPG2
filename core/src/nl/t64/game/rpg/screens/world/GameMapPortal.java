package nl.t64.game.rpg.screens.world;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.math.Rectangle;
import nl.t64.game.rpg.Utils;
import nl.t64.game.rpg.screens.world.entity.Direction;
import nl.t64.game.rpg.subjects.CollisionObserver;


class GameMapPortal extends GameMapRelocator implements CollisionObserver {

    private GameMapPortal(Rectangle rectangle,
                          String fromMapName,
                          String toMapName,
                          String toMapLocation,
                          Color fadeColor) {
        super(rectangle,
              fromMapName,
              toMapName,
              toMapLocation,
              fadeColor);

        Utils.getBrokerManager().collisionObservers.addObserver(this);
    }

    static GameMapPortal create(MapObject mapObject, String fromMapName) {
        RectangleMapObject rectObject = (RectangleMapObject) mapObject;
        return new GameMapPortal(rectObject.getRectangle(),
                                 fromMapName,
                                 rectObject.getName(),
                                 createToMapLocation(rectObject),
                                 Color.BLACK);
    }

    @Override
    public void onNotifyCollision(Rectangle playerBoundingBox, Direction playerDirection) {
        if (playerBoundingBox.overlaps(rectangle)) {
            Utils.getMapManager().collisionPortal(this, playerDirection);
        }
    }

}
