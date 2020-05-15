package nl.t64.game.rpg.events.character;

import com.badlogic.gdx.math.Vector2;
import lombok.AllArgsConstructor;
import nl.t64.game.rpg.events.Event;


@AllArgsConstructor
public class PositionEvent implements Event {
    public final Vector2 position;
}
