package nl.t64.game.rpg.screens.world.mapobjects;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.math.Rectangle;
import nl.t64.game.rpg.Utils;
import nl.t64.game.rpg.components.cutscene.CutsceneContainer;
import nl.t64.game.rpg.constants.ScreenType;
import nl.t64.game.rpg.screens.world.entity.Direction;
import nl.t64.game.rpg.subjects.CollisionObserver;


public class GameMapCutscene extends GameMapObject implements CollisionObserver {

    private final String cutsceneId;

    public GameMapCutscene(RectangleMapObject rectObject) {
        super.rectangle = rectObject.getRectangle();
        this.cutsceneId = rectObject.getProperties().get("type", String.class);

        Utils.getBrokerManager().collisionObservers.addObserver(this);
    }

    @Override
    public void onNotifyCollision(Rectangle playerBoundingBox, Direction playerDirection) {
        if (playerBoundingBox.overlaps(rectangle)) {
            startCutscene();
        }
    }

    private void startCutscene() {
        CutsceneContainer cutscenes = Utils.getGameData().getCutscenes();
        if (!cutscenes.isPlayed(cutsceneId)) {
            cutscenes.setPlayed(cutsceneId);
            Utils.getScreenManager().getWorldScreen().doBeforeLoadScreen();
            Utils.getBrokerManager().mapObservers.notifyMapWillChange(
                    () -> Utils.getScreenManager().setScreen(ScreenType.valueOf(cutsceneId.toUpperCase())),
                    Color.BLACK);
        }
    }

}
