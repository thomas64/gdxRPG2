package nl.t64.game.rpg.screens.world;

import com.badlogic.gdx.maps.objects.TextureMapObject;
import lombok.Getter;
import nl.t64.game.rpg.Utils;


class GameMapQuestTexture {

    @Getter
    private final TextureMapObject texture;
    private final String questId;
    private final boolean isVisibleIfComplete;
    private final String taskId;
    @Getter
    private boolean isVisible;

    GameMapQuestTexture(TextureMapObject textObject) {
        this.texture = textObject;
        this.questId = textObject.getProperties().get("type", String.class);
        this.isVisibleIfComplete = textObject.getProperties().get("visibleIfComplete", Boolean.class);
        this.taskId = textObject.getProperties().get("task", String.class);
        this.isVisible = textObject.getProperties().get("isVisible", Boolean.class);
    }

    void update() {
        boolean isFinished = Utils.getGameData().getQuests().getQuestById(questId).isFinished();
        boolean isComplete = Utils.getGameData().getQuests().getQuestById(questId).isTaskComplete(taskId);
        isVisible = (isFinished || isComplete) == isVisibleIfComplete;
    }

}