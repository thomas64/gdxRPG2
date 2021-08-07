package nl.t64.game.rpg.screens.world.entity

import nl.t64.game.rpg.screens.world.entity.events.Event


class InputEmpty : InputComponent() {

    override fun receive(event: Event) {
        // empty
    }

    override fun dispose() {
        // empty
    }

    override fun update(entity: Entity, dt: Float) {
        // empty
    }

}
