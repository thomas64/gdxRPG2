package nl.t64.game.rpg.screens.world.entity.events;

import lombok.AllArgsConstructor;
import nl.t64.game.rpg.screens.world.entity.Direction;


@AllArgsConstructor
public class DirectionEvent implements Event {
    public final Direction direction;
}
