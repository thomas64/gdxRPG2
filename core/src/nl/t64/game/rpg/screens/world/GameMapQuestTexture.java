package nl.t64.game.rpg.screens.world;

import com.badlogic.gdx.maps.MapObject;
import lombok.Getter;
import nl.t64.game.rpg.Utils;


class GameMapQuestTexture {

    @Getter
    private final MapObject texture;
    private final String questId;
    private final boolean isVisibleIfComplete;
    private final String taskId;
    @Getter
    private boolean isVisible;

    GameMapQuestTexture(MapObject mapObject) {
        this.texture = mapObject;
        this.questId = mapObject.getProperties().get("type", String.class);
        this.isVisibleIfComplete = mapObject.getProperties().get("visibleIfComplete", Boolean.class);
        this.taskId = mapObject.getProperties().get("task", String.class);
        this.isVisible = mapObject.getProperties().get("isVisible", Boolean.class);
    }

    void update() {
        boolean isFinished = Utils.getGameData().getQuests().getQuestById(questId).isFinished();
        boolean isComplete = Utils.getGameData().getQuests().getQuestById(questId).isTaskComplete(taskId);
        isVisible = (isFinished || isComplete) == isVisibleIfComplete;
    }

}