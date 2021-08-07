package nl.t64.game.rpg.screens.world.messagedialog

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.InputMultiplexer
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.graphics.g2d.Sprite
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.scenes.scene2d.actions.Actions
import com.badlogic.gdx.scenes.scene2d.ui.Dialog
import com.badlogic.gdx.scenes.scene2d.ui.Label
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle
import com.badlogic.gdx.scenes.scene2d.ui.Window.WindowStyle
import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable
import com.badlogic.gdx.utils.Align
import com.badlogic.gdx.utils.GdxRuntimeException
import com.badlogic.gdx.utils.Null
import nl.t64.game.rpg.Utils
import nl.t64.game.rpg.Utils.audioManager
import nl.t64.game.rpg.Utils.resourceManager
import nl.t64.game.rpg.audio.AudioCommand
import nl.t64.game.rpg.audio.AudioEvent
import nl.t64.game.rpg.constants.Constant


private const val SPRITE_PARCHMENT = "sprites/parchment.png"
private const val DIALOG_FONT = "fonts/fff_tusj.ttf"
private const val FONT_SIZE = 30

private const val DIALOG_INIT_HEIGHT = 100L
private const val DIALOG_PAD = 60f

class MessageDialog(private val multiplexer: InputMultiplexer) {

    private val stage = Stage()
    private val font: BitmapFont = resourceManager.getTrueTypeAsset(DIALOG_FONT, FONT_SIZE)
    private lateinit var label: Label
    private val dialog: Dialog = createDialog()

    @Null
    private var actionAfterHide: (() -> Unit)? = null

    init {
        applyListeners()
    }

    fun dispose() {
        stage.dispose()
        try {
            font.dispose()
        } catch (e: GdxRuntimeException) {
            // font is already exposed.
        }
    }

    fun setActionAfterHide(actionAfterHide: () -> Unit) {
        this.actionAfterHide = actionAfterHide
    }

    fun show(message: String, audioEvent: AudioEvent) {
        fillDialog(message)
        audioManager.handle(AudioCommand.SE_PLAY_ONCE, audioEvent)
        dialog.show(stage)
        Gdx.input.inputProcessor = stage
        Utils.setGamepadInputProcessor(stage)
    }

    fun update(dt: Float) {
        stage.act(dt)
        stage.draw()
    }

    private fun createDialog(): Dialog {
        // styles
        val labelStyle = LabelStyle(font, Color.BLACK)
        val windowStyle = WindowStyle()
        windowStyle.titleFont = font
        windowStyle.titleFontColor = Color.BLACK

        // actors
        label = Label("no message", labelStyle)
        label.setAlignment(Align.center)

        // dialog
        val newDialog = Dialog("", windowStyle)
        newDialog.titleLabel.setAlignment(Align.center)
        newDialog.padLeft(DIALOG_PAD)
        newDialog.padRight(DIALOG_PAD)

        val sprite = Sprite(resourceManager.getTextureAsset(SPRITE_PARCHMENT))
        newDialog.background(SpriteDrawable(sprite))

        newDialog.setKeepWithinStage(true)
        newDialog.isModal = true
        newDialog.isMovable = false
        newDialog.isResizable = false

        return newDialog
    }

    private fun fillDialog(message: String) {
        val dialogHeight = ((message.lines().count() * FONT_SIZE) + DIALOG_INIT_HEIGHT).toFloat()
        label.setText(message)
        dialog.contentTable.clear()
        dialog.contentTable.defaults().width(label.prefWidth)
        dialog.background.minHeight = dialogHeight
        dialog.text(label)
    }

    private fun applyListeners() {
        dialog.addListener(MessageDialogListener { hide() })
    }

    private fun hide() {
        Gdx.input.inputProcessor = multiplexer
        Utils.setGamepadInputProcessor(multiplexer)
        audioManager.handle(AudioCommand.SE_PLAY_ONCE, AudioEvent.SE_CONVERSATION_NEXT)
        actionAfterHide?.let { hideWithAction(it) } ?: dialog.hide()
    }

    private fun hideWithAction(action: () -> Unit) {
        stage.addAction(Actions.sequence(
            Actions.run { dialog.hide() },
            Actions.delay(Constant.DIALOG_FADE_OUT_DURATION),
            Actions.run {
                action.invoke()
                actionAfterHide = null
            }
        ))
    }

}
