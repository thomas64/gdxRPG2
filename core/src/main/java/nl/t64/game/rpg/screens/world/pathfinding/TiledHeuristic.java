package nl.t64.game.rpg.screens.world.pathfinding;

import com.badlogic.gdx.ai.pfa.Heuristic;


class TiledHeuristic<N extends TiledNode> implements Heuristic<N> {

    @Override
    public float estimate(N node, N endNode) {
        return (float) Math.abs(endNode.x - node.x) + (float) Math.abs(endNode.y - node.y);
    }

}
