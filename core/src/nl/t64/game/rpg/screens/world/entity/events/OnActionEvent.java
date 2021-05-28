package nl.t64.game.rpg.screens.world.entity.events;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import nl.t64.game.rpg.screens.world.entity.Direction;


public record OnActionEvent(Rectangle checkRect,
                            Direction playerDirection,
                            Vector2 playerPosition) implements Event {
}
