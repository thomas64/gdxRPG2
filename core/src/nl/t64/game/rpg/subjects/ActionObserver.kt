package nl.t64.game.rpg.subjects

import com.badlogic.gdx.math.Rectangle
import com.badlogic.gdx.math.Vector2
import nl.t64.game.rpg.screens.world.entity.Direction


interface ActionObserver {

    fun onNotifyActionPressed(checkRect: Rectangle, playerDirection: Direction, playerPosition: Vector2)

}
