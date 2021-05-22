package nl.t64.game.rpg.screens.world.mapobjects;

import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import nl.t64.game.rpg.Utils;
import nl.t64.game.rpg.components.event.Event;
import nl.t64.game.rpg.screens.world.entity.Direction;
import nl.t64.game.rpg.subjects.ActionObserver;


public class GameMapEventChecker extends GameMapObject implements ActionObserver {

    private final String eventId;

    public GameMapEventChecker(RectangleMapObject rectObject) {
        super.rectangle = rectObject.getRectangle();
        this.eventId = rectObject.getName();

        Utils.getBrokerManager().actionObservers.addObserver(this);
    }

    @Override
    public void onNotifyActionPressed(Rectangle checkRect, Direction playerDirection, Vector2 playerPostion) {
        if (checkRect.overlaps(rectangle)) {
            possibleStartEvent();
        }
    }

    private void possibleStartEvent() {
        Event event = Utils.getGameData().getEvents().getEventById(eventId);
        event.possibleStart();
    }

}
