package nl.t64.game.rpg.screens.world;

import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;

import java.util.Optional;


class GameMapHero extends GameMapNpc {

    final boolean hasBeenRecruited;

    GameMapHero(MapObject mapObject) {
        super(mapObject);
        RectangleMapObject rectObject = (RectangleMapObject) mapObject;
        this.hasBeenRecruited = createHasBeenRecruited(rectObject);
    }

    private boolean createHasBeenRecruited(RectangleMapObject rectObject) {
        Optional<Boolean> newHasBeenRecruited = Optional.ofNullable(
                rectObject.getProperties().get("hasBeenRecruited", Boolean.class));
        return newHasBeenRecruited.orElse(false);
    }

}
