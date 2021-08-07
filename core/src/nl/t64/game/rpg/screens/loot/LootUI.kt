package nl.t64.game.rpg.screens.loot

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.scenes.scene2d.ui.Label
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle
import com.badlogic.gdx.scenes.scene2d.ui.Window
import nl.t64.game.rpg.Utils
import nl.t64.game.rpg.components.loot.Loot
import nl.t64.game.rpg.screens.inventory.tooltip.LootSlotTooltip


private const val WINDOW_PADDING_BOTTOM = 10f
private const val LABEL_PADDING_TOP = 15f

internal class LootUI(lootScreen: LootScreen, loot: Loot, title: String) {

    private val tooltip = LootSlotTooltip()
    private val lootSlotsContainer = LootSlotsTable(lootScreen, loot, tooltip).apply {
        lootSlots.background = Utils.createTopBorder()
    }
    private val lootWindow: Window = Utils.createDefaultWindow(title, lootSlotsContainer.lootSlots).apply {
        setPosition((Gdx.graphics.width / 2f) - (width / 2f),
                    (Gdx.graphics.height / 2f) - (height / 2f) + WINDOW_PADDING_BOTTOM)
    }
    private val buttonLabel = Label(createText(), LabelStyle(BitmapFont(), Color.BLACK)).apply {
        setPosition((Gdx.graphics.width / 2f) - (width / 2f),
                    (Gdx.graphics.height / 2f) - (lootWindow.height / 2f) - LABEL_PADDING_TOP)
    }

    fun show(stage: Stage) {
        stage.addActor(lootWindow)
        tooltip.addToStage(stage)
        stage.addActor(buttonLabel)

        stage.keyboardFocus = lootSlotsContainer.lootSlots
    }

    private fun createText(): String {
        return if (Utils.isGamepadConnected()) {
            "[A] Take      [Select] Tooltip      [B] Back"
        } else {
            "[A] Take      [T] Tooltip      [Esc] Back"
        }
    }

}
