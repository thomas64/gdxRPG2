package nl.t64.game.rpg.screens.world.entity

import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import nl.t64.game.rpg.constants.Constant
import nl.t64.game.rpg.screens.world.entity.events.*


class GraphicsEnemy(spriteId: String) : GraphicsComponent() {

    init {
        frameDuration = Constant.NORMAL_FRAMES
        loadWalkingAnimation(spriteId)
    }

    override fun receive(event: Event) {
        if (event is LoadEntityEvent) {
            state = event.state!!
            direction = event.direction!!
        }
        if (event is StateEvent) {
            state = event.state
        }
        if (event is DirectionEvent) {
            direction = event.direction
        }
        if (event is PositionEvent) {
            position = event.position
        }
        if (event is SpeedEvent) {
            setNewFrameDuration(event.moveSpeed)
        }
    }

    override fun update(dt: Float) {
        setFrame(dt)
    }

    override fun render(batch: Batch) {
        batch.draw(currentFrame, position.x, position.y, Constant.TILE_SIZE, Constant.TILE_SIZE)
    }

    override fun renderOnMiniMap(entity: Entity, batch: Batch, shapeRenderer: ShapeRenderer) {
        // empty
    }

}
