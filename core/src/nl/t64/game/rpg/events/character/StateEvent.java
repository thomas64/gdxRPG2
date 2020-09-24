package nl.t64.game.rpg.events.character;

import lombok.AllArgsConstructor;
import nl.t64.game.rpg.components.character.CharacterState;
import nl.t64.game.rpg.events.Event;


@AllArgsConstructor
public class StateEvent implements Event {
    public final CharacterState state;
}
