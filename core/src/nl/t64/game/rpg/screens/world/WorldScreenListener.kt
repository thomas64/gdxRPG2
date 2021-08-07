package nl.t64.game.rpg.screens.world

import com.badlogic.gdx.Input
import com.badlogic.gdx.InputAdapter
import nl.t64.game.rpg.constants.Constant
import nl.t64.game.rpg.screens.inventory.InventoryScreen
import nl.t64.game.rpg.screens.menu.MenuPause
import nl.t64.game.rpg.screens.questlog.QuestLogScreen


internal class WorldScreenListener(
    private val doBeforeLoadScreen: () -> Unit,
    private val showHidePartyWindowFunction: () -> Unit,
    private val openMiniMap: () -> Unit,
    private val setShowGrid: () -> Unit,
    private val setShowObjects: () -> Unit,
    private val setShowDebug: () -> Unit
) : InputAdapter() {

    override fun keyDown(keycode: Int): Boolean {
        when (keycode) {
            Constant.KEYCODE_SELECT,
            Input.Keys.M -> {
                openMiniMap.invoke()
                return false
            }
        }

        when (keycode) {
            Constant.KEYCODE_START,
            Constant.KEYCODE_TOP,
            Constant.KEYCODE_LEFT,
            Input.Keys.ESCAPE,
            Input.Keys.I,
            Input.Keys.L -> doBeforeLoadScreen.invoke()
        }

        when (keycode) {
            Constant.KEYCODE_START, Input.Keys.ESCAPE -> MenuPause.load()
            Constant.KEYCODE_TOP, Input.Keys.I -> InventoryScreen.load()
            Constant.KEYCODE_LEFT, Input.Keys.L -> QuestLogScreen.load()
            Constant.KEYCODE_R3, Input.Keys.P -> showHidePartyWindowFunction.invoke()
            Input.Keys.F10 -> setShowGrid.invoke()
            Input.Keys.F11 -> setShowObjects.invoke()
            Input.Keys.F12 -> setShowDebug.invoke()
        }

        return false
    }

}
