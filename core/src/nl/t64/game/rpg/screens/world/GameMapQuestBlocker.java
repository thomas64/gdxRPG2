package nl.t64.game.rpg.screens.world;

import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.math.Rectangle;
import nl.t64.game.rpg.Utils;


class GameMapQuestBlocker {

    final Rectangle rectangle;
    final String questId;

    GameMapQuestBlocker(MapObject mapObject) {
        RectangleMapObject rectObject = (RectangleMapObject) mapObject;

        this.rectangle = rectObject.getRectangle();
        this.questId = rectObject.getProperties().get("type", String.class);
    }

    boolean isQuestOfThisBlockerFinished() {
        return Utils.getGameData().getQuests().getQuestById(questId).isFinished();
    }

}
