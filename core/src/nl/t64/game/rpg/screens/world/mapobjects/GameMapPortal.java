package nl.t64.game.rpg.screens.world.mapobjects;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.math.Rectangle;
import nl.t64.game.rpg.Utils;
import nl.t64.game.rpg.screens.world.entity.Direction;
import nl.t64.game.rpg.subjects.CollisionObserver;


public class GameMapPortal extends GameMapRelocator implements CollisionObserver {

    public GameMapPortal(RectangleMapObject rectObject, String fromMapName) {
        super(rectObject.getRectangle(),
              fromMapName,
              rectObject.getName(),
              createToMapLocation(rectObject),
              Color.BLACK);

        Utils.getBrokerManager().collisionObservers.addObserver(this);
    }

    @Override
    public void onNotifyCollision(Rectangle playerBoundingBox, Direction playerDirection) {
        if (playerBoundingBox.overlaps(rectangle)) {
            Utils.getMapManager().collisionPortal(this, playerDirection);
        }
    }

}
