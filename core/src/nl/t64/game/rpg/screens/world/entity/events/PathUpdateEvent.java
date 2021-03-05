package nl.t64.game.rpg.screens.world.entity.events;

import com.badlogic.gdx.ai.pfa.DefaultGraphPath;
import lombok.AllArgsConstructor;
import nl.t64.game.rpg.screens.world.pathfinding.TiledNode;


@AllArgsConstructor
public class PathUpdateEvent implements Event {
    public final DefaultGraphPath<TiledNode> path;
}
