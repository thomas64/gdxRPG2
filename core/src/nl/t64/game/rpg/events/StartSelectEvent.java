package nl.t64.game.rpg.events;

import com.badlogic.gdx.math.Vector3;
import lombok.AllArgsConstructor;
import lombok.Getter;


@AllArgsConstructor
public class StartSelectEvent implements Event {
    @Getter
    private final Vector3 mouseCoordinates;
}
