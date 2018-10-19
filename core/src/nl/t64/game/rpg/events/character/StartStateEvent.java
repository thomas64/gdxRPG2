package nl.t64.game.rpg.events.character;

import lombok.AllArgsConstructor;
import lombok.Getter;
import nl.t64.game.rpg.constants.CharacterState;
import nl.t64.game.rpg.events.Event;


@AllArgsConstructor
public class StartStateEvent implements Event {
    @Getter
    private final CharacterState state;
}
