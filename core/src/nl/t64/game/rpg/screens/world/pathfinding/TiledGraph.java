package nl.t64.game.rpg.screens.world.pathfinding;

import com.badlogic.gdx.ai.pfa.Connection;
import com.badlogic.gdx.ai.pfa.DefaultConnection;
import com.badlogic.gdx.ai.pfa.DefaultGraphPath;
import com.badlogic.gdx.ai.pfa.indexed.IndexedAStarPathFinder;
import com.badlogic.gdx.ai.pfa.indexed.IndexedGraph;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;

import java.util.stream.IntStream;


public class TiledGraph implements IndexedGraph<TiledNode> {

    private final int width;
    private final int height;
    private final Array<TiledNode> nodes;

    public TiledGraph(int width, int height) {
        this.width = width;
        this.height = height;
        this.nodes = new Array<>(width * height);
        this.fillNodes();
        this.addConnections();
    }

    public DefaultGraphPath<TiledNode> findPath(Vector2 startPoint, Vector2 endPoint) {
        final var pathFinder = new IndexedAStarPathFinder<>(this);
        final var tiledHeuristic = new TiledHeuristic<>();
        final var path = new DefaultGraphPath<TiledNode>();
        pathFinder.searchNodePath(getNode(startPoint), getNode(endPoint), tiledHeuristic, path);
        return path;
    }

    private void fillNodes() {
        IntStream.range(0, width).forEach(
                x -> IntStream.range(0, height).forEach(
                        y -> nodes.add(new TiledNode(x, y))));
    }

    private void addConnections() {
        IntStream.range(0, width).forEach(x -> {
            final int index = x * height;
            IntStream.range(0, height).forEach(y -> {
                final TiledNode tiledNode = nodes.get(index + y);
                if (x > 0) addConnection(tiledNode, -1, 0);
                if (y > 0) addConnection(tiledNode, 0, -1);
                if (x < width - 1) addConnection(tiledNode, 1, 0);
                if (y < height - 1) addConnection(tiledNode, 0, 1);
            });
        });
    }

    private void addConnection(TiledNode tiledNode, int xOffset, int yOffset) {
        final TiledNode target = getNode(tiledNode.x + xOffset, tiledNode.y + yOffset);
        if (!target.isCollision) {
            tiledNode.connections.add(new DefaultConnection<>(tiledNode, target));
        }
    }

    private TiledNode getNode(Vector2 point) {
        return getNode((int) point.x, (int) point.y);
    }

    private TiledNode getNode(int x, int y) {
        return nodes.get(x * height + y);
    }

    @Override
    public int getIndex(TiledNode tiledNode) {
        return tiledNode.getIndex(height);
    }

    @Override
    public int getNodeCount() {
        return nodes.size;
    }

    @Override
    public Array<Connection<TiledNode>> getConnections(TiledNode fromNode) {
        return fromNode.connections;
    }

}
