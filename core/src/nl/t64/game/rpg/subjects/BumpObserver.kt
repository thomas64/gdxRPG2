package nl.t64.game.rpg.subjects

import com.badlogic.gdx.math.Rectangle
import com.badlogic.gdx.math.Vector2


interface BumpObserver {

    fun onNotifyBump(biggerBoundingBox: Rectangle, checkRect: Rectangle, playerPosition: Vector2)

}
