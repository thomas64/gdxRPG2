package nl.t64.game.rpg.screens.world.entity.events;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import lombok.AllArgsConstructor;
import nl.t64.game.rpg.screens.world.entity.Direction;


@AllArgsConstructor
public class OnActionEvent implements Event {
    public final Rectangle checkRect;
    public final Direction playerDirection;
    public final Vector2 playerPosition;
}
