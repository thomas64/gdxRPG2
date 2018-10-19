package nl.t64.game.rpg.events.character;

import com.badlogic.gdx.math.Vector3;
import lombok.AllArgsConstructor;
import lombok.Getter;
import nl.t64.game.rpg.events.Event;


@AllArgsConstructor
public class StartSelectEvent implements Event {
    @Getter
    private final Vector3 mouseCoordinates;
}
