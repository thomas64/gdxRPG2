package nl.t64.game.rpg.screens.world;

import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import nl.t64.game.rpg.Utils;
import nl.t64.game.rpg.subjects.BlockObserver;

import java.util.Optional;


class GameMapQuestBlocker extends GameMapObject implements BlockObserver {

    private final String questId;
    private final boolean isActiveIfComplete;
    private final String taskId;
    private boolean isActive;

    GameMapQuestBlocker(MapObject mapObject) {
        RectangleMapObject rectObject = (RectangleMapObject) mapObject;

        this.rectangle = rectObject.getRectangle();
        this.questId = rectObject.getProperties().get("type", String.class);
        this.isActiveIfComplete = rectObject.getProperties().get("activeIfComplete", Boolean.class);
        this.taskId = rectObject.getProperties().get("task", String.class);
        this.isActive = rectObject.getProperties().get("isActive", Boolean.class);

        if (this.isActive) {
            Utils.getBrokerManager().blockObservers.addObserver(this);
        }
    }

    @Override
    public Optional<Rectangle> getBlockerFor(Rectangle boundingBox) {
        if (isActive && boundingBox.overlaps(rectangle)) {
            return Optional.of(rectangle);
        } else {
            return Optional.empty();
        }
    }

    @Override
    public boolean isBlocking(Vector2 point) {
        return isActive && rectangle.contains(point);
    }

    void update() {
        final boolean isFinished = Utils.getGameData().getQuests().getQuestById(questId).isFinished();
        final boolean isComplete = Utils.getGameData().getQuests().getQuestById(questId).isTaskComplete(taskId);

        final boolean before = isActive;
        isActive = (isFinished || isComplete) == isActiveIfComplete;
        final boolean after = isActive;
        if (before != after) {
            changeBlocker();
        }
    }

    private void changeBlocker() {
        if (isActive) {
            Utils.getBrokerManager().blockObservers.addObserver(this);
        } else {
            Utils.getBrokerManager().blockObservers.removeObserver(this);
        }
        Utils.getMapManager().setTiledGraph();
    }

}
