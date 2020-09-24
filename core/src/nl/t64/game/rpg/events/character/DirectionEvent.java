package nl.t64.game.rpg.events.character;

import lombok.AllArgsConstructor;
import nl.t64.game.rpg.components.character.Direction;
import nl.t64.game.rpg.events.Event;


@AllArgsConstructor
public class DirectionEvent implements Event {
    public final Direction direction;
}
