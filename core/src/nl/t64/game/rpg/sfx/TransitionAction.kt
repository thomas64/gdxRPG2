package nl.t64.game.rpg.sfx

import com.badlogic.gdx.scenes.scene2d.Action
import com.badlogic.gdx.scenes.scene2d.actions.Actions
import nl.t64.game.rpg.constants.Constant


class TransitionAction : Action() {

    private lateinit var type: TransitionType
    private var duration: Float = 0f

    companion object {
        @JvmStatic
        fun transition(type: TransitionType): TransitionAction {
            val action = Actions.action(TransitionAction::class.java)
            action.type = type
            action.duration = Constant.FADE_DURATION
            return action
        }
    }

    override fun act(dt: Float): Boolean {
        val actor = super.getTarget()
        when (type) {
            TransitionType.FADE_IN -> actor.addAction(Actions.fadeOut(duration))
            TransitionType.FADE_OUT -> actor.addAction(Actions.fadeIn(duration))
        }
        return true
    }

}
