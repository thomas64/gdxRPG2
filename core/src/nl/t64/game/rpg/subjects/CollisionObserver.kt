package nl.t64.game.rpg.subjects

import com.badlogic.gdx.math.Rectangle
import nl.t64.game.rpg.screens.world.entity.Direction


interface CollisionObserver {

    fun onNotifyCollision(playerBoundingBox: Rectangle, playerDirection: Direction)

}
