package nl.t64.game.rpg.events.character;

import lombok.AllArgsConstructor;
import nl.t64.game.rpg.constants.Direction;
import nl.t64.game.rpg.events.Event;


@AllArgsConstructor
public class StartDirectionEvent implements Event {
    public final Direction direction;
}
