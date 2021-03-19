package nl.t64.game.rpg.screens.world;

import com.badlogic.gdx.maps.objects.RectangleMapObject;


class GameMapHero extends GameMapNpc {

    final boolean hasBeenRecruited;

    GameMapHero(RectangleMapObject rectObject) {
        super(rectObject);
        this.hasBeenRecruited = createHasBeenRecruited(rectObject);
    }

    private boolean createHasBeenRecruited(RectangleMapObject rectObject) {
        return rectObject.getProperties().get("hasBeenRecruited", false, Boolean.class);
    }

}
