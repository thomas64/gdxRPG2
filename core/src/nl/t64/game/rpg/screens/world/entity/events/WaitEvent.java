package nl.t64.game.rpg.screens.world.entity.events;

import com.badlogic.gdx.math.Vector2;
import lombok.AllArgsConstructor;


@AllArgsConstructor
public class WaitEvent implements Event {
    public final Vector2 npcPosition;
    public final Vector2 playerPosition;
}
