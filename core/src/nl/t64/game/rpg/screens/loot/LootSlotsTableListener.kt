package nl.t64.game.rpg.screens.loot

import com.badlogic.gdx.Input
import com.badlogic.gdx.scenes.scene2d.InputEvent
import com.badlogic.gdx.scenes.scene2d.InputListener
import nl.t64.game.rpg.constants.Constant


internal class LootSlotsTableListener(
    private val closeScreenFunction: Runnable,
    private val toggleTooltip: Runnable,
    private val takeItemFunction: Runnable,
    private val selectNewSlot: (Int) -> Unit,
    private val slotsInRow: Int
) : InputListener() {

    override fun keyDown(event: InputEvent, keycode: Int): Boolean {
        when (keycode) {
            Constant.KEYCODE_RIGHT, Input.Keys.ESCAPE -> closeScreenFunction.run()
            Constant.KEYCODE_SELECT, Input.Keys.T -> toggleTooltip.run()
            Constant.KEYCODE_BOTTOM, Input.Keys.ENTER, Input.Keys.A -> takeItemFunction.run()
            Input.Keys.UP -> selectNewSlot.invoke(-slotsInRow)
            Input.Keys.DOWN -> selectNewSlot.invoke(slotsInRow)
            Input.Keys.LEFT -> selectNewSlot.invoke(-1)
            Input.Keys.RIGHT -> selectNewSlot.invoke(1)
        }
        return true
    }

}
