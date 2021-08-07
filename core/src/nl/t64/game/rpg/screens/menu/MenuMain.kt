package nl.t64.game.rpg.screens.menu

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.scenes.scene2d.ui.Image
import com.badlogic.gdx.scenes.scene2d.ui.Table
import com.badlogic.gdx.scenes.scene2d.ui.TextButton
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle
import com.badlogic.gdx.utils.ScreenUtils
import nl.t64.game.rpg.Utils.audioManager
import nl.t64.game.rpg.Utils.resourceManager
import nl.t64.game.rpg.audio.AudioCommand
import nl.t64.game.rpg.audio.AudioEvent
import nl.t64.game.rpg.constants.ScreenType


private const val BACKGROUND = "sprites/titlescreen.jpg"
private const val MENU_ITEM_START = "Start"
private const val MENU_ITEM_SETTINGS = "Settings"
private const val MENU_ITEM_CREDITS = "Credits"
private const val MENU_ITEM_EXIT = "Exit"
private const val NUMBER_OF_ITEMS = 4

class MenuMain : MenuScreen() {

    private lateinit var listenerKeyVertical: ListenerKeyVertical

    init {
        super.startScreen = ScreenType.MENU_MAIN
        super.selectedMenuIndex = 0
    }

    override fun setupScreen() {
        audioManager.handle(AudioCommand.BGM_PLAY_LOOP, AudioEvent.BGM_TITLE)
        val texture = resourceManager.getTextureAsset(BACKGROUND)
        setBackground(Image(texture))
        setFontColor()
        table = createTable()
        applyListeners()
        stage.addActor(table)
        stage.keyboardFocus = table
        setCurrentTextButtonToSelected()
    }

    override fun render(dt: Float) {
        ScreenUtils.clear(Color.BLACK)
        stage.act(dt)
        listenerKeyVertical.updateSelectedIndex(selectedMenuIndex)
        stage.draw()
    }

    private fun selectMenuItem() {
        when (selectedMenuIndex) {
            0 -> processButton(ScreenType.MENU_MAIN, ScreenType.MENU_LOAD)
            1 -> processButton(ScreenType.MENU_MAIN, ScreenType.MENU_SETTINGS)
            2 -> processButton(ScreenType.MENU_MAIN, ScreenType.MENU_CREDITS)
            3 -> processExitButton()
            else -> throw IllegalArgumentException("SelectedIndex not found.")
        }
    }

    private fun processExitButton() {
        Gdx.app.exit()
    }

    private fun createTable(): Table {
        // styles
        val buttonStyle = TextButtonStyle()
        buttonStyle.font = menuFont
        buttonStyle.fontColor = fontColor

        // actors
        val startButton = TextButton(MENU_ITEM_START, TextButtonStyle(buttonStyle))
        val settingsButton = TextButton(MENU_ITEM_SETTINGS, TextButtonStyle(buttonStyle))
        val creditsButton = TextButton(MENU_ITEM_CREDITS, TextButtonStyle(buttonStyle))
        val exitButton = TextButton(MENU_ITEM_EXIT, TextButtonStyle(buttonStyle))

        // table
        val newTable = Table()
        newTable.setFillParent(true)
        newTable.defaults().center()
        newTable.add(startButton).row()
        newTable.add(settingsButton).row()
        newTable.add(creditsButton).row()
        newTable.add(exitButton)
        val logo = stage.actors.peek()
        newTable
            .top().padTop((logo.height * logo.scaleY) + LOGO_PAD + PAD_TOP)
            .right().padRight(((logo.width * logo.scaleX) / 2f) - (newTable.prefWidth / 2f) + LOGO_PAD)
        return newTable
    }

    private fun applyListeners() {
        listenerKeyVertical = ListenerKeyVertical({ super.updateMenuIndex(it) }, NUMBER_OF_ITEMS)
        val listenerKeyConfirm = ListenerKeyConfirm { selectMenuItem() }
        table.addListener(listenerKeyVertical)
        table.addListener(listenerKeyConfirm)
    }

}
