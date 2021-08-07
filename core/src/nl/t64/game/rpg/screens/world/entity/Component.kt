package nl.t64.game.rpg.screens.world.entity

import nl.t64.game.rpg.screens.world.entity.events.Event


internal interface Component {

    fun receive(event: Event)
    fun dispose()

}
