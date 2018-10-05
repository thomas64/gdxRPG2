package nl.t64.game.rpg.events;

import com.badlogic.gdx.math.Vector2;
import lombok.AllArgsConstructor;
import lombok.Getter;


@AllArgsConstructor
public class PositionEvent implements Event {
    @Getter
    private final Vector2 position;
}
