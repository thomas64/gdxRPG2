package nl.t64.game.rpg.screens.world.mapobjects;

import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.math.Rectangle;
import nl.t64.game.rpg.Utils;
import nl.t64.game.rpg.components.event.Event;
import nl.t64.game.rpg.screens.world.entity.Direction;
import nl.t64.game.rpg.subjects.CollisionObserver;


public class GameMapEventDiscover extends GameMapObject implements CollisionObserver {

    private final String eventId;

    public GameMapEventDiscover(RectangleMapObject rectObject) {
        super.rectangle = rectObject.getRectangle();
        this.eventId = rectObject.getName();

        Utils.getBrokerManager().collisionObservers.addObserver(this);
    }

    @Override
    public void onNotifyCollision(Rectangle playerBoundingBox, Direction playerDirection) {
        if (playerBoundingBox.overlaps(rectangle)) {
            possibleStartEvent();
        }
    }

    private void possibleStartEvent() {
        Event event = Utils.getGameData().getEvents().getEventById(eventId);
        event.possibleStart();
    }

}
