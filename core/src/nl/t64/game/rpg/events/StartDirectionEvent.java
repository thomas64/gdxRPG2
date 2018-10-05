package nl.t64.game.rpg.events;

import lombok.AllArgsConstructor;
import lombok.Getter;
import nl.t64.game.rpg.constants.Direction;


@AllArgsConstructor
public class StartDirectionEvent implements Event {
    @Getter
    private final Direction direction;
}
