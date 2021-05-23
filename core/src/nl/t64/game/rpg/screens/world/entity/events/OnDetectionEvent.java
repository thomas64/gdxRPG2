package nl.t64.game.rpg.screens.world.entity.events;

import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Vector2;


public record OnDetectionEvent(Circle detectionRange, Vector2 ownPosition) implements Event {
}
