package nl.t64.game.rpg.events;

import com.badlogic.gdx.math.Vector2;
import lombok.AllArgsConstructor;
import lombok.Getter;


@AllArgsConstructor
public class WaitEvent implements Event {
    @Getter
    private final Vector2 npcPosition;
    @Getter
    private final Vector2 playerPosition;
}
