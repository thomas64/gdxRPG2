package nl.t64.game.rpg.screens.world;

import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import nl.t64.game.rpg.Utils;
import nl.t64.game.rpg.components.event.Event;

import java.util.function.BiConsumer;


class GameMapEvent extends GameMapObject {

    private final String eventId;

    GameMapEvent(MapObject mapObject) {
        RectangleMapObject rectObject = (RectangleMapObject) mapObject;

        this.rectangle = rectObject.getRectangle();
        this.eventId = rectObject.getProperties().get("type", String.class);
    }

    void startConversation(BiConsumer<String, String> notifyShowConversationDialog) {
        Event event = Utils.getGameData().getEvents().getEventById(eventId);
        event.startConversation(notifyShowConversationDialog);
    }

}
