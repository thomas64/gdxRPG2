package nl.t64.game.rpg.screens.world.mapobjects;

import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.math.Rectangle;
import nl.t64.game.rpg.Utils;
import nl.t64.game.rpg.components.quest.QuestGraph;
import nl.t64.game.rpg.screens.world.entity.Direction;
import nl.t64.game.rpg.subjects.CollisionObserver;


public class GameMapQuestDiscoverObject extends GameMapObject implements CollisionObserver {

    private final String questId;
    private final String taskId;

    public GameMapQuestDiscoverObject(RectangleMapObject rectObject) {
        super.rectangle = rectObject.getRectangle();
        this.questId = rectObject.getProperties().get("type", String.class);
        this.taskId = rectObject.getProperties().get("task", String.class);

        Utils.getBrokerManager().collisionObservers.addObserver(this);
    }

    @Override
    public void onNotifyCollision(Rectangle playerBoundingBox, Direction playerDirection) {
        if (playerBoundingBox.overlaps(rectangle)) {
            setQuestTaskComplete();
        }
    }

    private void setQuestTaskComplete() {
        QuestGraph quest = Utils.getGameData().getQuests().getQuestById(questId);
        quest.setTaskComplete(taskId);
    }

}
