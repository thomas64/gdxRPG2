package nl.t64.game.rpg.events.menu;

import com.badlogic.gdx.math.Vector3;
import lombok.AllArgsConstructor;
import lombok.Getter;
import nl.t64.game.rpg.events.Event;


@AllArgsConstructor
public class MouseMoveEvent implements Event {
    @Getter
    private final Vector3 mouseCoordinates;
}
