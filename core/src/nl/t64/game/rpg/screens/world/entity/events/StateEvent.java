package nl.t64.game.rpg.screens.world.entity.events;

import lombok.AllArgsConstructor;
import nl.t64.game.rpg.screens.world.entity.EntityState;


@AllArgsConstructor
public class StateEvent implements Event {
    public final EntityState state;
}
