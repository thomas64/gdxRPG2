package nl.t64.game.rpg.screens.world.mapobjects;

import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.math.Rectangle;
import nl.t64.game.rpg.Utils;
import nl.t64.game.rpg.screens.world.entity.Direction;
import nl.t64.game.rpg.subjects.CollisionObserver;


public class GameMapCutscene extends GameMapObject implements CollisionObserver {

    private final String cutsceneId;

    public GameMapCutscene(RectangleMapObject rectObject) {
        super.rectangle = rectObject.getRectangle();
        this.cutsceneId = rectObject.getName();

        Utils.getBrokerManager().collisionObservers.addObserver(this);
    }

    @Override
    public void onNotifyCollision(Rectangle playerBoundingBox, Direction playerDirection) {
        if (playerBoundingBox.overlaps(rectangle)) {
            Utils.getBrokerManager().mapObservers.notifyStartCutscene(cutsceneId);
        }
    }

}
