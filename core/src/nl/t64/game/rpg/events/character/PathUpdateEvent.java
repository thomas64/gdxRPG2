package nl.t64.game.rpg.events.character;

import com.badlogic.gdx.ai.pfa.DefaultGraphPath;
import lombok.AllArgsConstructor;
import nl.t64.game.rpg.events.Event;
import nl.t64.game.rpg.screens.world.pathfinding.TiledNode;


@AllArgsConstructor
public class PathUpdateEvent implements Event {
    public final DefaultGraphPath<TiledNode> path;
}
