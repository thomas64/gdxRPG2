package nl.t64.game.rpg.subjects

import com.badlogic.gdx.math.Rectangle
import com.badlogic.gdx.math.Vector2


interface BlockObserver {

    fun getBlockerFor(boundingBox: Rectangle): Rectangle?
    fun isBlocking(point: Vector2): Boolean

}
