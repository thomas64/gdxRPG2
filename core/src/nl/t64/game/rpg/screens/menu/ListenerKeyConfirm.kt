package nl.t64.game.rpg.screens.menu

import com.badlogic.gdx.Input
import com.badlogic.gdx.scenes.scene2d.InputEvent
import com.badlogic.gdx.scenes.scene2d.InputListener
import nl.t64.game.rpg.constants.Constant


internal class ListenerKeyConfirm(selectItemFunction: () -> Unit) : InputListener() {

    private val listenerInput: ListenerInput = ListenerInput(selectItemFunction)

    override fun keyDown(event: InputEvent, keycode: Int): Boolean {
        when (keycode) {
            Constant.KEYCODE_BOTTOM, Input.Keys.ENTER -> listenerInput.inputConfirm()
        }
        return true
    }

}
