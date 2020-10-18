package nl.t64.game.rpg.screens.world;

import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;


class GameMapHero extends GameMapNpc {

    final boolean hasBeenRecruited;

    GameMapHero(MapObject mapObject) {
        super(mapObject);
        RectangleMapObject rectObject = (RectangleMapObject) mapObject;
        this.hasBeenRecruited = createHasBeenRecruited(rectObject);
    }

    private boolean createHasBeenRecruited(RectangleMapObject rectObject) {
        return rectObject.getProperties().get("hasBeenRecruited", false, Boolean.class);
    }

}
