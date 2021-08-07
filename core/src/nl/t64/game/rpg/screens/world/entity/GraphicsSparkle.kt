package nl.t64.game.rpg.screens.world.entity

import com.badlogic.gdx.graphics.g2d.Animation
import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import com.badlogic.gdx.utils.Array
import nl.t64.game.rpg.Utils
import nl.t64.game.rpg.constants.Constant
import nl.t64.game.rpg.screens.world.entity.events.Event
import nl.t64.game.rpg.screens.world.entity.events.LoadEntityEvent


private const val SPARKLE_PATH = "sprites/objects/sparkle.png"
private const val ANIMATION_LENGTH = 35

class GraphicsSparkle : GraphicsComponent() {

    private val sparkleAnimation: Animation<TextureRegion> = createAnimation()

    override fun receive(event: Event) {
        if (event is LoadEntityEvent) {
            position = event.position
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

    override fun setFrame(dt: Float) {
        frameTime = (frameTime + dt) % 12
        currentFrame = sparkleAnimation.getKeyFrame(frameTime)
    }

    private fun createAnimation(): Animation<TextureRegion> {
        val textures = Utils.getSplitTexture(SPARKLE_PATH, Constant.TILE_SIZE.toInt())

        val frames = Array<TextureRegion>(ANIMATION_LENGTH)
        (0..29).forEach { frames.add(textures[4][0]) }
        frames.add(textures[3][2])
        frames.add(textures[3][1])
        frames.add(textures[0][1])
        frames.add(textures[3][1])
        frames.add(textures[3][2])
        return Animation(Constant.FAST_FRAMES, frames, Animation.PlayMode.LOOP)
    }

}
