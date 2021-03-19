package nl.t64.game.rpg.screens.world;

import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.math.Rectangle;
import nl.t64.game.rpg.Utils;
import nl.t64.game.rpg.components.event.Event;
import nl.t64.game.rpg.screens.world.entity.Direction;
import nl.t64.game.rpg.subjects.CollisionObserver;


class GameMapEvent extends GameMapObject implements CollisionObserver {

    private final String eventId;

    GameMapEvent(MapObject mapObject) {
        RectangleMapObject rectObject = (RectangleMapObject) mapObject;

        this.rectangle = rectObject.getRectangle();
        this.eventId = rectObject.getProperties().get("type", String.class);

        Utils.getBrokerManager().collisionObservers.addObserver(this);
    }

    @Override
    public void onNotifyCollision(Rectangle playerBoundingBox, Direction playerDirection) {
        if (playerBoundingBox.overlaps(rectangle)) {
            startConversation();
        }
    }

    // todo, een event zal niet altijd alleen een conversation zijn. oplossen wanneer dat moment daar is.
    private void startConversation() {
        Event event = Utils.getGameData().getEvents().getEventById(eventId);
        event.startConversation();
    }

}
