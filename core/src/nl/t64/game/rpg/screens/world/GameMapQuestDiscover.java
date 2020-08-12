package nl.t64.game.rpg.screens.world;

import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.math.Rectangle;
import nl.t64.game.rpg.Utils;
import nl.t64.game.rpg.components.quest.QuestGraph;


class GameMapQuestDiscover {

    final Rectangle rectangle;
    final String questId;
    final String taskId;

    GameMapQuestDiscover(MapObject mapObject) {
        RectangleMapObject rectObject = (RectangleMapObject) mapObject;

        this.rectangle = rectObject.getRectangle();
        this.questId = rectObject.getProperties().get("type", String.class);
        this.taskId = rectObject.getProperties().get("task", String.class);
    }

    void locationDiscovered() {
        QuestGraph quest = Utils.getGameData().getQuests().getQuestById(questId);
        quest.setLocationDiscoverd(taskId);
    }

}
