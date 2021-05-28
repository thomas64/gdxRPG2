package nl.t64.game.rpg.screens.world.entity.events;

import com.badlogic.gdx.math.Vector2;


public record WaitEvent(Vector2 npcPosition,
                        Vector2 playerPosition) implements Event {
}
