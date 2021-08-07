package nl.t64.game.rpg.screens.world.entity

import com.badlogic.gdx.graphics.g2d.Animation
import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import com.badlogic.gdx.utils.Array
import nl.t64.game.rpg.Utils
import nl.t64.game.rpg.components.door.Door
import nl.t64.game.rpg.constants.Constant
import nl.t64.game.rpg.screens.world.entity.events.Event
import nl.t64.game.rpg.screens.world.entity.events.LoadEntityEvent
import nl.t64.game.rpg.screens.world.entity.events.StateEvent


class GraphicsDoor(private val door: Door) : GraphicsComponent() {

    private val openAnimation: Animation<TextureRegion> = loadOpenAnimation()

    override fun receive(event: Event) {
        if (event is LoadEntityEvent) {
            position = event.position
            state = event.state!!
        }
        if (event is StateEvent) {
            state = event.state
        }
    }

    override fun update(dt: Float) {
        setFrame(dt)
    }

    override fun render(batch: Batch) {
        batch.draw(currentFrame, position.x, position.y, door.width, door.height)
    }

    override fun renderOnMiniMap(entity: Entity, batch: Batch, shapeRenderer: ShapeRenderer) {
        // empty
    }

    override fun setFrame(dt: Float) {
        frameTime = when {
            frameTime >= 1f -> 1f
            state == EntityState.OPENED -> frameTime + dt
            else -> 0f
        }
        currentFrame = openAnimation.getKeyFrame(frameTime)
    }

    override fun getAnimation(): Animation<TextureRegion> {
        return openAnimation
    }

    private fun loadOpenAnimation(): Animation<TextureRegion> {
        val textureFrames = Utils.getDoorImage(door.spriteId, door.width.toInt())
        val frames = Array(arrayOf(textureFrames[0][0], textureFrames[1][0], textureFrames[2][0], textureFrames[3][0]))
        return Animation(Constant.FAST_FRAMES, frames, Animation.PlayMode.NORMAL)
    }

}
