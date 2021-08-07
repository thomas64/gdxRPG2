package nl.t64.game.rpg.screens.inventory.tooltip

import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.graphics.g2d.NinePatch
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.scenes.scene2d.ui.Window
import com.badlogic.gdx.scenes.scene2d.ui.Window.WindowStyle
import com.badlogic.gdx.scenes.scene2d.utils.NinePatchDrawable
import com.badlogic.gdx.utils.Align
import nl.t64.game.rpg.Utils.resourceManager
import nl.t64.game.rpg.screens.inventory.itemslot.ItemSlot


private const val SPRITE_SLOT = "sprites/tooltip.png"
private const val PADDING = 10f

private fun createWindowStyle(): WindowStyle {
    val texture = resourceManager.getTextureAsset(SPRITE_SLOT)
    val ninepatch = NinePatch(texture, 1, 1, 1, 1)
    val drawable = NinePatchDrawable(ninepatch)
    return WindowStyle(BitmapFont(), Color.GREEN, drawable)
}

abstract class BaseTooltip {

    val window: Window = Window("", createWindowStyle()).apply {
        defaults().align(Align.left)
        pad(PADDING)
        pack()
        isVisible = false
    }

    open fun toggle(itemSlot: ItemSlot) {
        throw IllegalStateException("Implement this method in child.")
    }

    open fun toggleCompare(itemSlot: ItemSlot) {
        // do nothing. some tooltips don't need compare.
    }

    fun addToStage(stage: Stage) {
        stage.addActor(window)
    }

    fun hide() {
        window.clearActions()
        window.isVisible = false
    }

}
