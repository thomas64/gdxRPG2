package nl.t64.game.rpg.screens.world.entity

import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import com.badlogic.gdx.math.Rectangle
import com.badlogic.gdx.math.Vector2
import nl.t64.game.rpg.Utils.brokerManager
import nl.t64.game.rpg.constants.Constant
import nl.t64.game.rpg.screens.world.entity.events.CollisionEvent
import kotlin.math.roundToInt


private const val WANDER_BOX_SIZE = 240f
private const val WANDER_BOX_POSITION = -96f

abstract class PhysicsComponent : Component {

    lateinit var entity: Entity
    lateinit var state: EntityState
    lateinit var direction: Direction
    var velocity = 0f
    var boundingBox = Rectangle()
    lateinit var wanderBox: Rectangle
    var oldPosition = Vector2()
    var currentPosition = Vector2()
    var boundingBoxWidthPercentage = 0f
    var boundingBoxHeightPercentage = 0f

    override fun dispose() {
        // empty
    }

    abstract fun update(entity: Entity, dt: Float)

    abstract fun debug(shapeRenderer: ShapeRenderer)

    fun doesBoundingBoxOverlapsBlockers(): Boolean {
        return brokerManager.blockObservers.getCurrentBlockersFor(boundingBox)
            .filter { boundingBox != it }
            .any { boundingBox.overlaps(it) }
    }

    open fun setBoundingBox() {
        val width = Constant.TILE_SIZE * boundingBoxWidthPercentage
        val height = Constant.TILE_SIZE * boundingBoxHeightPercentage
        val widthReduction = 1.00f - boundingBoxWidthPercentage
        val x = currentPosition.x + (Constant.TILE_SIZE * (widthReduction / 2))
        val y = currentPosition.y
        boundingBox.set(x, y, width, height)
    }

    fun setWanderBox() {
        wanderBox = Rectangle(currentPosition.x + WANDER_BOX_POSITION,
                              currentPosition.y + WANDER_BOX_POSITION,
                              WANDER_BOX_SIZE, WANDER_BOX_SIZE)
    }

    fun relocate(dt: Float) {
        when (state) {
            EntityState.WALKING, EntityState.FLYING -> move(dt)
            EntityState.ALIGNING -> alignToGrid()
        }
    }

    fun move(dt: Float) {
        oldPosition.set(currentPosition.x.roundToInt().toFloat(), currentPosition.y.roundToInt().toFloat())

        when (direction) {
            Direction.NORTH -> currentPosition.y += velocity * dt
            Direction.SOUTH -> currentPosition.y -= velocity * dt
            Direction.WEST -> currentPosition.x -= velocity * dt
            Direction.EAST -> currentPosition.x += velocity * dt
            Direction.NONE -> throw IllegalArgumentException("Direction 'NONE' is not usable.")
        }
        setRoundPosition()
    }

    fun moveBack() {
        when (direction) {
            Direction.NORTH -> currentPosition.y -= 1f
            Direction.SOUTH -> currentPosition.y += 1f
            Direction.WEST -> currentPosition.x += 1f
            Direction.EAST -> currentPosition.x -= 1f
            Direction.NONE -> throw IllegalArgumentException("Direction 'NONE' is not usable.")
        }
        setRoundPosition()
    }

    fun setRoundPosition() {
        currentPosition.set(currentPosition.x.roundToInt().toFloat(), currentPosition.y.roundToInt().toFloat())
        setBoundingBox()
    }

    fun getRectangle(): Rectangle =
        Rectangle(currentPosition.x, currentPosition.y, Constant.TILE_SIZE, Constant.TILE_SIZE)

    fun checkObstacles() {
        when (state) {
            EntityState.WALKING, EntityState.FLYING -> {
                val moveBack1 = checkWanderBox()
                val moveBack2 = checkBlockers()
                if (moveBack1 || moveBack2) {
                    entity.send(CollisionEvent())
                }
            }
        }
    }

    private fun checkWanderBox(): Boolean {
        var moveBack = false
        while (!wanderBox.contains(boundingBox)) {
            moveBack()
            moveBack = true
        }
        return moveBack
    }

    private fun checkBlockers(): Boolean {
        var moveBack = false
        while (doesBoundingBoxOverlapsBlockers()) {
            moveBack()
            moveBack = true
        }
        return moveBack
    }

    private fun alignToGrid() {
        val roundedX = (currentPosition.x / Constant.TILE_SIZE).roundToInt() * Constant.TILE_SIZE
        val roundedY = (currentPosition.y / Constant.TILE_SIZE).roundToInt() * Constant.TILE_SIZE
        currentPosition.set(roundedX, roundedY)
    }

}
