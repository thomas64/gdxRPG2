package nl.t64.game.rpg.screens.world.entity


abstract class InputComponent : Component {

    lateinit var direction: Direction

    abstract fun update(entity: Entity, dt: Float)

    open fun reset() {
        // empty
    }

}
