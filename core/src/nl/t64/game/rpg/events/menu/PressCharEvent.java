package nl.t64.game.rpg.events.menu;

import lombok.AllArgsConstructor;
import lombok.Getter;
import nl.t64.game.rpg.events.Event;


@AllArgsConstructor
public class PressCharEvent implements Event {
    @Getter
    private final Character character;

}
