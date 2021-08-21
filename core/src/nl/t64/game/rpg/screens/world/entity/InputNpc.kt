package nl.t64.game.rpg.screens.world.entity

import com.badlogic.gdx.math.MathUtils
import com.badlogic.gdx.math.Vector2
import nl.t64.game.rpg.screens.world.entity.events.*
import kotlin.math.abs


private const val DEFAULT_STATE_TIME = 5f

class InputNpc : InputComponent() {

    private var stateTime = 0f
    private lateinit var state: EntityState
    private lateinit var originalDirection: Direction

    override fun receive(event: Event) {
        if (event is LoadEntityEvent) {
            state = event.state!!
            direction = event.direction!!
            originalDirection = direction
        }
        if (event is CollisionEvent) {
            stateTime = 0f
        }
        if (event is WaitEvent) {
            handleEvent(event)
        }
    }

    override fun update(entity: Entity, dt: Float) {
        stateTime -= dt
        if (stateTime < 0) {
            setRandom()
        }
        entity.send(StateEvent(state))
        entity.send(DirectionEvent(direction))
    }

    private fun setRandom() {
        stateTime = MathUtils.random(1.0f, 2.0f)

        when (state) {
            EntityState.INVISIBLE -> {
            }
            EntityState.WALKING -> {
                state = EntityState.IDLE
            }
            EntityState.IDLE -> {
                state = EntityState.WALKING
                direction = Direction.getRandom()
            }
            EntityState.IMMOBILE, EntityState.IDLE_ANIMATING -> {
                direction = originalDirection
            }
            else -> throw IllegalArgumentException("EntityState '$state' not usable.")
        }
    }

    private fun handleEvent(event: WaitEvent) {
        val npcPosition: Vector2 = event.npcPosition
        val playerPosition: Vector2 = event.playerPosition
        stateTime = DEFAULT_STATE_TIME
        if (state == EntityState.WALKING) {
            state = EntityState.IDLE
        }
        turnToPlayer(npcPosition, playerPosition)
    }

    private fun turnToPlayer(npcPos: Vector2, playerPos: Vector2) {
        when {
            npcPos.x < playerPos.x && abs(npcPos.y - playerPos.y) < abs(npcPos.x - playerPos.x) -> direction = Direction.EAST
            npcPos.x > playerPos.x && abs(npcPos.y - playerPos.y) < abs(npcPos.x - playerPos.x) -> direction = Direction.WEST
            npcPos.y < playerPos.y && abs(npcPos.y - playerPos.y) > abs(npcPos.x - playerPos.x) -> direction = Direction.NORTH
            npcPos.y > playerPos.y && abs(npcPos.y - playerPos.y) > abs(npcPos.x - playerPos.x) -> direction = Direction.SOUTH
        }
    }

}
