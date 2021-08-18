package nl.t64.game.rpg.screens.inventory

import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.scenes.scene2d.ui.Table
import com.badlogic.gdx.scenes.scene2d.ui.Window
import nl.t64.game.rpg.Utils.gameData
import nl.t64.game.rpg.Utils.screenManager
import nl.t64.game.rpg.components.party.HeroItem
import nl.t64.game.rpg.constants.Constant
import nl.t64.game.rpg.screens.ScreenUI


object InventoryUtils {

    private var selectedHero: HeroItem? = null

    fun getSelectedHero(): HeroItem {
        return selectedHero!!
    }

    fun getSelectedHeroId(): String {
        val party = gameData.party
        if (selectedHero == null || !party.containsExactlyEqualTo(selectedHero!!)) {
            selectedHero = party.getHero(0)
        }
        return selectedHero!!.id
    }

    fun selectPreviousHero() {
        selectedHero = gameData.party.getPreviousHero(selectedHero!!)
    }

    fun selectNextHero() {
        selectedHero = gameData.party.getNextHero(selectedHero!!)
    }

    fun setWindowDeselected(container: Table) {
        val parent = container.parent as Window
        parent.titleLabel.style.fontColor = Color.BLACK
    }

    fun setWindowSelected(container: Table) {
        val parent = container.parent as Window
        parent.titleLabel.style.fontColor = Constant.DARK_RED
    }

    fun getScreenUI(): ScreenUI = screenManager.getCurrentParchmentScreen().getScreenUI()

}
