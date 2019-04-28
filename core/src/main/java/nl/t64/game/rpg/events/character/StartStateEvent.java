package nl.t64.game.rpg.events.character;

import lombok.AllArgsConstructor;
import nl.t64.game.rpg.constants.CharacterState;
import nl.t64.game.rpg.events.Event;


@AllArgsConstructor
public class StartStateEvent implements Event {
    public final CharacterState state;
}
