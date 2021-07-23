package nl.t64.game.rpg.subjects

import com.badlogic.gdx.math.Rectangle
import com.badlogic.gdx.math.Vector2
import nl.t64.game.rpg.constants.Constant


class BlockSubject {

    private val observers: MutableList<BlockObserver> = ArrayList()

    fun addObserver(observer: BlockObserver) {
        observers.add(observer)
    }

    fun removeObserver(observer: BlockObserver) {
        observers.remove(observer)
    }

    fun removeAllObservers() {
        observers.clear()
    }

    fun getCurrentBlockersFor(boundingBox: Rectangle): List<Rectangle> {
        return ArrayList(observers).mapNotNull { it.getBlockerFor(boundingBox) }
    }

    fun isBlockerBlockingGridPoint(x: Float, y: Float): Boolean {
        return ArrayList(observers).any { it.isBlocking(getGridPoint(x, y)) }
    }

    private fun getGridPoint(x: Float, y: Float): Vector2 {
        return Vector2(x * Constant.HALF_TILE_SIZE + 1f, y * Constant.HALF_TILE_SIZE + 1f)
    }

}
