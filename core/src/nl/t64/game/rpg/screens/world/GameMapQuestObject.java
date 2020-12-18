package nl.t64.game.rpg.screens.world;

import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import nl.t64.game.rpg.Utils;
import nl.t64.game.rpg.components.quest.QuestGraph;


class GameMapQuestObject extends GameMapObject {

    private final String questId;
    private final String taskId;

    GameMapQuestObject(MapObject mapObject) {
        RectangleMapObject rectObject = (RectangleMapObject) mapObject;

        this.rectangle = rectObject.getRectangle();
        this.questId = rectObject.getProperties().get("type", String.class);
        this.taskId = rectObject.getProperties().get("task", String.class);
    }

    void setQuestTaskComplete() {
        QuestGraph quest = Utils.getGameData().getQuests().getQuestById(questId);
        quest.setTaskComplete(taskId);
    }

}
