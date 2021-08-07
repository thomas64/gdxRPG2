package nl.t64.game.rpg.screens.world.entity.events

import com.badlogic.gdx.math.Rectangle
import com.badlogic.gdx.math.Vector2
import nl.t64.game.rpg.screens.world.entity.Direction


class OnActionEvent(val checkRect: Rectangle,
                    val playerDirection: Direction,
                    val playerPosition: Vector2) : Event
