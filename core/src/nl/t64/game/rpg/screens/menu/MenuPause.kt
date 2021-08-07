package nl.t64.game.rpg.screens.menu

import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.scenes.scene2d.ui.Table
import com.badlogic.gdx.scenes.scene2d.ui.TextButton
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle
import com.badlogic.gdx.utils.ScreenUtils
import nl.t64.game.rpg.Utils.audioManager
import nl.t64.game.rpg.Utils.createScreenshot
import nl.t64.game.rpg.Utils.mapManager
import nl.t64.game.rpg.Utils.screenManager
import nl.t64.game.rpg.audio.AudioCommand
import nl.t64.game.rpg.audio.AudioEvent
import nl.t64.game.rpg.constants.ScreenType


private const val MENU_ITEM_CONTINUE = "Continue"
private const val MENU_ITEM_LOAD_GAME = "Load Game"
private const val MENU_ITEM_SETTINGS = "Settings"
private const val MENU_ITEM_MAIN_MENU = "Main Menu"

private const val NUMBER_OF_ITEMS = 4
private const val EXIT_INDEX = 0

private val DIALOG_MESSAGE = """
    All progress after the last save will be lost.
    Are you sure you want to exit?""".trimIndent()

class MenuPause : MenuScreen() {

    private lateinit var listenerKeyVertical: ListenerKeyVertical

    companion object {
        fun load() {
            audioManager.handle(AudioCommand.SE_PLAY_ONCE, AudioEvent.SE_MENU_CONFIRM)
            val menuPause = screenManager.getMenuScreen(ScreenType.MENU_PAUSE)
            menuPause.setBackground(createScreenshot(true))
            screenManager.setScreen(ScreenType.MENU_PAUSE)
            menuPause.updateMenuIndex(EXIT_INDEX)
        }
    }

    init {
        super.startScreen = ScreenType.MENU_PAUSE
        super.selectedMenuIndex = EXIT_INDEX
    }

    override fun setupScreen() {
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
        audioManager.fadeBgmBgs()
        listenerKeyVertical.updateSelectedIndex(selectedMenuIndex)
        stage.draw()
    }

    private fun selectMenuItem() {
        when (selectedMenuIndex) {
            0 -> processContinueButton()
            1 -> processButton(ScreenType.MENU_PAUSE, ScreenType.MENU_LOAD)
            2 -> processButton(ScreenType.MENU_PAUSE, ScreenType.MENU_SETTINGS)
            3 -> processMainMenuButton()
            else -> throw IllegalArgumentException("SelectedIndex not found.")
        }
    }

    private fun processContinueButton() {
        screenManager.setScreen(ScreenType.WORLD)
    }

    private fun processMainMenuButton() {
        DialogQuestion({ openMenuMain() }, DIALOG_MESSAGE).show(stage)
    }

    private fun openMenuMain() {
        mapManager.disposeOldMaps()
        screenManager.setScreen(ScreenType.MENU_MAIN)
    }

    private fun createTable(): Table {
        // styles
        val buttonStyle = TextButtonStyle()
        buttonStyle.font = menuFont
        buttonStyle.fontColor = fontColor

        // actors
        val continueButton = TextButton(MENU_ITEM_CONTINUE, TextButtonStyle(buttonStyle))
        val loadGameButton = TextButton(MENU_ITEM_LOAD_GAME, TextButtonStyle(buttonStyle))
        val settingsButton = TextButton(MENU_ITEM_SETTINGS, TextButtonStyle(buttonStyle))
        val mainMenuButton = TextButton(MENU_ITEM_MAIN_MENU, TextButtonStyle(buttonStyle))

        // table
        val newTable = Table()
        newTable.setFillParent(true)
        newTable.defaults().center()
        newTable.add(continueButton).row()
        newTable.add(loadGameButton).row()
        newTable.add(settingsButton).row()
        newTable.add(mainMenuButton)
        val logo = stage.actors.peek()
        newTable
            .top().padTop((logo.height * logo.scaleY) + LOGO_PAD + PAD_TOP)
            .right().padRight(((logo.width * logo.scaleX) / 2f) - (newTable.prefWidth / 2f) + LOGO_PAD)
        return newTable
    }

    private fun applyListeners() {
        listenerKeyVertical = ListenerKeyVertical({ super.updateMenuIndex(it) }, NUMBER_OF_ITEMS)
        val listenerKeyConfirm = ListenerKeyConfirm { selectMenuItem() }
        val listenerKeyCancel = ListenerKeyCancel({ super.updateMenuIndex(it) }, { selectMenuItem() }, EXIT_INDEX)
        val listenerKeyStart = ListenerKeyStart({ super.updateMenuIndex(it) }, { selectMenuItem() }, EXIT_INDEX)
        table.addListener(listenerKeyVertical)
        table.addListener(listenerKeyConfirm)
        table.addListener(listenerKeyCancel)
        table.addListener(listenerKeyStart)
    }

}
