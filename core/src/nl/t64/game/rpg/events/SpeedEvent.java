package nl.t64.game.rpg.events;

import lombok.AllArgsConstructor;
import lombok.Getter;


@AllArgsConstructor
public class SpeedEvent implements Event {
    @Getter
    private final float moveSpeed;
}
