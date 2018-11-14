package nl.t64.game.rpg.events.character;

import lombok.AllArgsConstructor;
import lombok.Getter;
import nl.t64.game.rpg.constants.Direction;
import nl.t64.game.rpg.events.Event;


@AllArgsConstructor
public class DirectionEvent implements Event {
    @Getter
    private final Direction direction;
}
