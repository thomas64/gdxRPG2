package nl.t64.game.rpg.screens.world.entity

import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import nl.t64.game.rpg.Utils.brokerManager
import nl.t64.game.rpg.audio.AudioEvent
import nl.t64.game.rpg.components.loot.Loot
import nl.t64.game.rpg.screens.world.entity.events.Event
import nl.t64.game.rpg.screens.world.entity.events.LoadEntityEvent
import nl.t64.game.rpg.screens.world.entity.events.OnActionEvent


class PhysicsSparkle(private val sparkle: Loot) : PhysicsComponent() {

    private var isSelected = false

    override fun receive(event: Event) {
        if (event is LoadEntityEvent) {
            currentPosition = event.position
        }
        if (event is OnActionEvent) {
            if (event.checkRect.overlaps(getRectangle())) {
                isSelected = true
            }
        }
    }

    override fun update(entity: Entity, dt: Float) {
        if (isSelected) {
            isSelected = false
            brokerManager.componentObservers.notifyShowFindDialog(sparkle, AudioEvent.SE_SPARKLE)
        }
    }

    override fun debug(shapeRenderer: ShapeRenderer) {
        // empty
    }

}
