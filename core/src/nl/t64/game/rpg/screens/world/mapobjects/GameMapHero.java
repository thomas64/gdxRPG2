package nl.t64.game.rpg.screens.world.mapobjects;

import com.badlogic.gdx.maps.objects.RectangleMapObject;
import lombok.Getter;


@Getter
public class GameMapHero extends GameMapNpc {

    private final boolean hasBeenRecruited;

    public GameMapHero(RectangleMapObject rectObject) {
        super(rectObject);
        this.hasBeenRecruited = createHasBeenRecruited(rectObject);
    }

    private boolean createHasBeenRecruited(RectangleMapObject rectObject) {
        return rectObject.getProperties().get("hasBeenRecruited", false, Boolean.class);
    }

}
