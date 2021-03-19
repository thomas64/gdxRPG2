package nl.t64.game.rpg.screens.world.pathfinding;

import com.badlogic.gdx.ai.pfa.Connection;
import com.badlogic.gdx.utils.Array;
import nl.t64.game.rpg.Utils;


public class TiledNode {

    private static final int MAX_CONNECTIONS = 4;

    public final int x;
    public final int y;
    final boolean isCollision;
    final Array<Connection<TiledNode>> connections;

    TiledNode(int x, int y) {
        this.x = x;
        this.y = y;
        this.isCollision = Utils.getBrokerManager().blockObservers.isCurrentlyBlocking(x, y);
        this.connections = new Array<>(MAX_CONNECTIONS);
    }

    int getIndex(int mapHeight) {
        return x * mapHeight + y;
    }

}
