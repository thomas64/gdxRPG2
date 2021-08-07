package nl.t64.game.rpg.screens.menu

import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.graphics.g2d.Sprite
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.scenes.scene2d.ui.Dialog
import com.badlogic.gdx.scenes.scene2d.ui.Label
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle
import com.badlogic.gdx.scenes.scene2d.ui.TextButton
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle
import com.badlogic.gdx.scenes.scene2d.ui.Window.WindowStyle
import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable
import com.badlogic.gdx.utils.Align
import nl.t64.game.rpg.Utils.audioManager
import nl.t64.game.rpg.Utils.resourceManager
import nl.t64.game.rpg.audio.AudioCommand
import nl.t64.game.rpg.audio.AudioEvent
import nl.t64.game.rpg.constants.Constant


private const val SPRITE_PARCHMENT = "sprites/parchment.png"
private const val DIALOG_FONT = "fonts/fff_tusj.ttf"
private const val FONT_SIZE = 30

private const val DIALOG_YES = "Yes"
private const val DIALOG_NO = "No"

private const val DIALOG_INIT_HEIGHT = 150L
private const val DIALOG_PAD_TOP = 20f
private const val DIALOG_PAD_BOTTOM = 40f
private const val BUTTON_SPACE_RIGHT = 100f

private const val NUMBER_OF_ITEMS = 2
private const val EXIT_INDEX = 1

internal class DialogQuestion(
    private val yesFunction: () -> Unit,
    private val message: String
) {
    private val dialogHeight: Float = ((message.lines().count() * FONT_SIZE) + DIALOG_INIT_HEIGHT).toFloat()
    private val dialogFont: BitmapFont = resourceManager.getTrueTypeAsset(DIALOG_FONT, FONT_SIZE)
    private val dialog: Dialog = createDialog()
    private var selectedIndex = 0

    init {
        applyListeners()
    }

    fun show(stage: Stage) {
        dialog.show(stage)
        updateIndex(EXIT_INDEX)
    }

    private fun updateIndex(newIndex: Int) {
        selectedIndex = newIndex
        setAllTextButtonsToBlack()
        setCurrentTextButtonToRed()
    }

    private fun selectDialogItem() {
        when (selectedIndex) {
            0 -> processYesButton()
            1 -> processNoButton()
            else -> throw  IllegalArgumentException("SelectedIndex not found.")
        }
    }

    private fun processYesButton() {
        dialog.hide()
        yesFunction.invoke()
    }

    private fun processNoButton() {
        audioManager.handle(AudioCommand.SE_STOP_ALL)
        audioManager.handle(AudioCommand.SE_PLAY_ONCE, AudioEvent.SE_MENU_BACK)
        dialog.hide()
    }

    private fun setAllTextButtonsToBlack() {
        dialog.buttonTable.children.forEach { (it as TextButton).style.fontColor = Color.BLACK }
    }

    private fun setCurrentTextButtonToRed() {
        (dialog.buttonTable.getChild(selectedIndex) as TextButton).style.fontColor = Constant.DARK_RED
    }

    private fun createDialog(): Dialog {
        // styles
        val labelStyle = LabelStyle(dialogFont, Color.BLACK)
        val buttonStyle = TextButtonStyle()
        buttonStyle.font = dialogFont
        buttonStyle.fontColor = Color.BLACK
        val windowStyle = WindowStyle()
        windowStyle.titleFont = dialogFont
        windowStyle.titleFontColor = Color.BLACK

        // actors
        val label = Label(message, labelStyle)
        label.setAlignment(Align.center)
        val yesButton = TextButton(DIALOG_YES, TextButtonStyle(buttonStyle))
        val noButton = TextButton(DIALOG_NO, TextButtonStyle(buttonStyle))

        // table
        val newDialog = Dialog("", windowStyle)
        newDialog.titleLabel.setAlignment(Align.center)
        newDialog.padTop(DIALOG_PAD_TOP)
        newDialog.padBottom(DIALOG_PAD_BOTTOM)
        newDialog.contentTable.defaults().width(label.prefWidth + BUTTON_SPACE_RIGHT)

        val sprite = Sprite(resourceManager.getTextureAsset(SPRITE_PARCHMENT))
        newDialog.background(SpriteDrawable(sprite))
        newDialog.background.minHeight = dialogHeight

        newDialog.setKeepWithinStage(true)
        newDialog.isModal = true
        newDialog.isMovable = false
        newDialog.isResizable = false
        newDialog.text(label)

        newDialog.buttonTable.add(yesButton).spaceRight(BUTTON_SPACE_RIGHT)
        newDialog.buttonTable.add(noButton)
        return newDialog
    }

    private fun applyListeners() {
        val listenerKeyHorizontal = ListenerKeyHorizontal({ updateIndex(it) }, NUMBER_OF_ITEMS)
        listenerKeyHorizontal.updateSelectedIndex(EXIT_INDEX)
        val listenerKeyConfirm = ListenerKeyConfirm { selectDialogItem() }
        val listenerKeyCancel = ListenerKeyCancel({ updateIndex(it) }, { selectDialogItem() }, EXIT_INDEX)
        dialog.addListener(listenerKeyHorizontal)
        dialog.addListener(listenerKeyConfirm)
        dialog.addListener(listenerKeyCancel)
    }

}
