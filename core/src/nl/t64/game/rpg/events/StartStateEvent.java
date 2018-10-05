package nl.t64.game.rpg.events;

import lombok.AllArgsConstructor;
import lombok.Getter;
import nl.t64.game.rpg.constants.EntityState;


@AllArgsConstructor
public class StartStateEvent implements Event {
    @Getter
    private final EntityState state;
}
