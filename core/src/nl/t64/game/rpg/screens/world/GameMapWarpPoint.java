package nl.t64.game.rpg.screens.world;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import nl.t64.game.rpg.Utils;
import nl.t64.game.rpg.screens.world.entity.Direction;
import nl.t64.game.rpg.subjects.ActionObserver;


class GameMapWarpPoint extends GameMapRelocator implements ActionObserver {

    GameMapWarpPoint(RectangleMapObject rectObject, String fromMapName) {
        super(rectObject.getRectangle(),
              fromMapName,
              rectObject.getName(),
              createToMapLocation(rectObject),
              Color.WHITE);

        Utils.getBrokerManager().actionObservers.addObserver(this);
    }

    @Override
    public void onNotifyActionPressed(Rectangle checkRect, Direction playerDirection, Vector2 playerPosition) {
        if (checkRect.overlaps(rectangle)) {
            Utils.getMapManager().checkWarpPoint(this, playerDirection);
        }
    }

}
