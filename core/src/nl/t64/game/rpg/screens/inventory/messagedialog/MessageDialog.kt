package nl.t64.game.rpg.screens.inventory.messagedialog

import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.graphics.g2d.Sprite
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.scenes.scene2d.ui.Dialog
import com.badlogic.gdx.scenes.scene2d.ui.Label
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle
import com.badlogic.gdx.scenes.scene2d.ui.Window.WindowStyle
import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable
import com.badlogic.gdx.utils.Align
import nl.t64.game.rpg.Utils.audioManager
import nl.t64.game.rpg.Utils.resourceManager
import nl.t64.game.rpg.audio.AudioCommand
import nl.t64.game.rpg.audio.AudioEvent


private const val SPRITE_PARCHMENT = "sprites/parchment.png"
private const val DIALOG_FONT = "fonts/fff_tusj.ttf"
private const val FONT_SIZE = 30
private const val DIALOG_INIT_HEIGHT = 100L
private const val DIALOG_PAD = 60f

class MessageDialog(private val message: String) {

    private val dialogHeight: Float = (message.lines().count() * FONT_SIZE + DIALOG_INIT_HEIGHT).toFloat()
    private val dialogFont: BitmapFont = resourceManager.getTrueTypeAsset(DIALOG_FONT, FONT_SIZE)
    private val dialog: Dialog = createDialog()

    init {
        applyListeners()
    }

    fun show(stage: Stage, event: AudioEvent) {
        audioManager.handle(AudioCommand.SE_PLAY_ONCE, event)
        dialog.show(stage)
    }

    fun setLeftAlignment() {
        (dialog.contentTable.getChild(0) as Label).setAlignment(Align.left)
    }

    private fun createDialog(): Dialog {
        // styles
        val labelStyle = LabelStyle(dialogFont, Color.BLACK)
        val windowStyle = WindowStyle()
        windowStyle.titleFont = dialogFont
        windowStyle.titleFontColor = Color.BLACK

        // actors
        val label = Label(message, labelStyle)
        label.setAlignment(Align.center)

        // dialog
        val newDialog = Dialog("", windowStyle)
        newDialog.titleLabel.setAlignment(Align.center)
        newDialog.padLeft(DIALOG_PAD)
        newDialog.padRight(DIALOG_PAD)
        newDialog.contentTable.defaults().width(label.prefWidth)

        val sprite = Sprite(resourceManager.getTextureAsset(SPRITE_PARCHMENT))
        newDialog.background(SpriteDrawable(sprite))
        newDialog.background.minHeight = dialogHeight

        newDialog.setKeepWithinStage(true)
        newDialog.isModal = true
        newDialog.isMovable = false
        newDialog.isResizable = false
        newDialog.text(label)

        return newDialog
    }

    private fun applyListeners() {
        dialog.addListener(MessageDialogListener { hide() })
    }

    private fun hide() {
        audioManager.handle(AudioCommand.SE_PLAY_ONCE, AudioEvent.SE_CONVERSATION_NEXT)
        dialog.hide()
    }

}
