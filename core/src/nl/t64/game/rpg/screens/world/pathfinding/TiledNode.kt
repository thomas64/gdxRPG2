package nl.t64.game.rpg.screens.world.pathfinding

import com.badlogic.gdx.ai.pfa.Connection
import com.badlogic.gdx.utils.Array
import nl.t64.game.rpg.Utils.brokerManager


private const val MAX_CONNECTIONS = 4

class TiledNode(val x: Int, val y: Int) {

    val isCollision: Boolean = brokerManager.blockObservers.isBlockerBlockingGridPoint(x.toFloat(), y.toFloat())
    val connections: Array<Connection<TiledNode>> = Array(MAX_CONNECTIONS)

    fun getIndex(mapHeight: Int): Int {
        return x * mapHeight + y
    }

}
