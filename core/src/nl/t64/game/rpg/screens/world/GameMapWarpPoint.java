package nl.t64.game.rpg.screens.world;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.maps.MapObject;


class GameMapWarpPoint extends GameMapPortal {

    GameMapWarpPoint(MapObject mapObject, String fromMapName) {
        super(mapObject, fromMapName, Color.WHITE);
    }

}
