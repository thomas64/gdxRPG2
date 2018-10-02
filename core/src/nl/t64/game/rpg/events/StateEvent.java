package nl.t64.game.rpg.events;

import lombok.AllArgsConstructor;
import lombok.Getter;
import nl.t64.game.rpg.constants.EntityState;


@AllArgsConstructor
public class StateEvent extends Event {
    @Getter
    private EntityState state;
}
