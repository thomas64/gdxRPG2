package nl.t64.game.rpg.screens.inventory.messagedialog

import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.scenes.scene2d.ui.Dialog
import com.badlogic.gdx.scenes.scene2d.ui.Label
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle
import com.badlogic.gdx.utils.Align
import com.badlogic.gdx.utils.Null
import nl.t64.game.rpg.Utils
import nl.t64.game.rpg.Utils.audioManager
import nl.t64.game.rpg.Utils.resourceManager
import nl.t64.game.rpg.audio.AudioCommand
import nl.t64.game.rpg.audio.AudioEvent


private const val FONT = "fonts/spectral_regular_24.ttf"
private const val FONT_SIZE = 24
private const val LINE_HEIGHT = 26f

private const val DIALOG_INIT_HEIGHT = 100L
private const val DIALOG_PAD = 60f

class MessageDialog(private val message: String) {

    private val dialogHeight: Float = (message.lines().count() * FONT_SIZE + DIALOG_INIT_HEIGHT).toFloat()
    private val font: BitmapFont = resourceManager.getTrueTypeAsset(FONT, FONT_SIZE).apply {
        data.setLineHeight(LINE_HEIGHT)
    }
    private val dialog: Dialog = createDialog()

    @Null
    private var actionAfterHide: (() -> Unit)? = null

    init {
        applyListeners()
    }

    fun setActionAfterHide(actionAfterHide: () -> Unit) {
        this.actionAfterHide = actionAfterHide
    }

    fun show(stage: Stage, event: AudioEvent) {
        audioManager.handle(AudioCommand.SE_PLAY_ONCE, event)
        dialog.show(stage)
    }

    fun setLeftAlignment() {
        (dialog.contentTable.getChild(0) as Label).setAlignment(Align.left)
    }

    private fun createDialog(): Dialog {
        val label = Label(message, LabelStyle(font, Color.BLACK))
        return Utils.createParchmentDialog(font).apply {
            padLeft(DIALOG_PAD)
            padRight(DIALOG_PAD)
            contentTable.defaults().width(label.prefWidth)
            background.minHeight = dialogHeight
            text(label)
        }
    }

    private fun applyListeners() {
        dialog.addListener(MessageDialogListener { hide() })
    }

    private fun hide() {
        audioManager.handle(AudioCommand.SE_PLAY_ONCE, AudioEvent.SE_CONVERSATION_NEXT)
        actionAfterHide?.let { hideWithAction(it) } ?: dialog.hide()
    }

    private fun hideWithAction(action: () -> Unit) {
        dialog.hide()
        action.invoke()
        actionAfterHide = null
    }

}
