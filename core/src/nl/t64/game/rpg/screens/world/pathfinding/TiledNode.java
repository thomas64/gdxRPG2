package nl.t64.game.rpg.screens.world.pathfinding;

import com.badlogic.gdx.ai.pfa.Connection;
import com.badlogic.gdx.utils.Array;


public class TiledNode {

    private static final int MAX_CONNECTIONS = 4;

    public final int x;
    public final int y;
    final boolean isCollision;
    final Array<Connection<TiledNode>> connections;

    TiledNode(int x, int y, boolean isCollision) {
        this.x = x;
        this.y = y;
        this.isCollision = isCollision;
        this.connections = new Array<>(MAX_CONNECTIONS);
    }

    int getIndex(int mapHeight) {
        return x * mapHeight + y;
    }

}
