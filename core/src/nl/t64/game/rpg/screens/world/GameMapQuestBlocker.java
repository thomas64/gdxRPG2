package nl.t64.game.rpg.screens.world;

import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.math.Rectangle;
import nl.t64.game.rpg.Utils;


class GameMapQuestBlocker {

    final Rectangle rectangle;
    private final String questId;
    private final boolean isActiveIfComplete;
    private final String taskId;
    boolean isActive;

    GameMapQuestBlocker(MapObject mapObject) {
        RectangleMapObject rectObject = (RectangleMapObject) mapObject;

        this.rectangle = rectObject.getRectangle();
        this.questId = rectObject.getProperties().get("type", String.class);
        this.isActiveIfComplete = rectObject.getProperties().get("activeIfComplete", Boolean.class);
        this.taskId = rectObject.getProperties().get("task", String.class);
        this.isActive = rectObject.getProperties().get("isActive", Boolean.class);
    }

    void update() {
        boolean isFinished = Utils.getGameData().getQuests().getQuestById(questId).isFinished();
        boolean isComplete = Utils.getGameData().getQuests().getQuestById(questId).isTaskComplete(taskId);
        isActive = (isFinished || isComplete) == isActiveIfComplete;
    }

}
