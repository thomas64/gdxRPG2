package nl.t64.game.rpg.events.character;

import lombok.AllArgsConstructor;
import nl.t64.game.rpg.events.Event;


@AllArgsConstructor
public class SpeedEvent implements Event {
    public final float moveSpeed;
}
