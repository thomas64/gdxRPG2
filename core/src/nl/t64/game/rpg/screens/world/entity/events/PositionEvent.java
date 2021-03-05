package nl.t64.game.rpg.screens.world.entity.events;

import com.badlogic.gdx.math.Vector2;
import lombok.AllArgsConstructor;


@AllArgsConstructor
public class PositionEvent implements Event {
    public final Vector2 position;
}
