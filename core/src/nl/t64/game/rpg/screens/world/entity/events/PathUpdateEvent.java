package nl.t64.game.rpg.screens.world.entity.events;

import com.badlogic.gdx.ai.pfa.DefaultGraphPath;
import nl.t64.game.rpg.screens.world.pathfinding.TiledNode;


public record PathUpdateEvent(DefaultGraphPath<TiledNode> path) implements Event {
}
