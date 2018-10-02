package nl.t64.game.rpg.events;

import com.badlogic.gdx.math.Vector2;
import lombok.AllArgsConstructor;
import lombok.Getter;


@AllArgsConstructor
public class StartPositionEvent extends Event {
    @Getter
    private Vector2 position;
}
