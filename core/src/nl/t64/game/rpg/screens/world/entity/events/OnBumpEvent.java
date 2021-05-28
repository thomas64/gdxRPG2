package nl.t64.game.rpg.screens.world.entity.events;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;


public record OnBumpEvent(Rectangle biggerBoundingBox,
                          Rectangle checkRect,
                          Vector2 playerPosition) implements Event {
}
