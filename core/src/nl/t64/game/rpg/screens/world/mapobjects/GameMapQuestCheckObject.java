package nl.t64.game.rpg.screens.world.mapobjects;

import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import nl.t64.game.rpg.Utils;
import nl.t64.game.rpg.components.quest.QuestGraph;
import nl.t64.game.rpg.screens.world.entity.Direction;
import nl.t64.game.rpg.subjects.ActionObserver;


public class GameMapQuestCheckObject extends GameMapObject implements ActionObserver {

    private final String questId;
    private final String taskId;

    public GameMapQuestCheckObject(RectangleMapObject rectObject) {
        super.rectangle = rectObject.getRectangle();
        this.questId = rectObject.getProperties().get("type", String.class);
        this.taskId = rectObject.getProperties().get("task", String.class);

        Utils.getBrokerManager().actionObservers.addObserver(this);
    }

    @Override
    public void onNotifyActionPressed(Rectangle checkRect, Direction playerDirection, Vector2 playerPosition) {
        if (checkRect.overlaps(rectangle)) {
            setQuestTaskComplete();
        }
    }

    private void setQuestTaskComplete() {
        QuestGraph quest = Utils.getGameData().getQuests().getQuestById(questId);
        quest.setTaskComplete(taskId);
    }

}
