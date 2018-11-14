package nl.t64.game.rpg.events.character;

import com.badlogic.gdx.math.Vector2;
import lombok.AllArgsConstructor;
import lombok.Getter;
import nl.t64.game.rpg.events.Event;


@AllArgsConstructor
public class PositionEvent implements Event {
    @Getter
    private final Vector2 position;
}
