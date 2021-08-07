package nl.t64.game.rpg.screens.menu

import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.scenes.scene2d.ui.Table
import com.badlogic.gdx.scenes.scene2d.ui.TextButton
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle
import com.badlogic.gdx.utils.ScreenUtils
import nl.t64.game.rpg.Utils.preferenceManager
import nl.t64.game.rpg.constants.ScreenType


private const val MENU_ITEM_FULL_SCREEN_ON = "Fullscreen: On"
private const val MENU_ITEM_FULL_SCREEN_OFF = "Fullscreen: Off"
private const val MENU_ITEM_MUSIC_ON = "Music: On"
private const val MENU_ITEM_MUSIC_OFF = "Music: Off"
private const val MENU_ITEM_SOUND_ON = "Sound: On"
private const val MENU_ITEM_SOUND_OFF = "Sound: Off"
private const val MENU_ITEM_DEBUG_MODE_ON = "Debug mode: On"
private const val MENU_ITEM_DEBUG_MODE_OFF = "Debug mode: Off"
private const val MENU_ITEM_CONTROLS = "View controls"
private const val MENU_ITEM_BACK = "Back"

private const val MENU_X = 604f
private const val NUMBER_OF_ITEMS = 6
private const val EXIT_INDEX = 5

class MenuSettings : MenuScreen() {

    private lateinit var fullscreenButton: TextButton
    private lateinit var musicButton: TextButton
    private lateinit var soundButton: TextButton
    private lateinit var debugModeButton: TextButton
    private lateinit var listenerKeyVertical: ListenerKeyVertical

    init {
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
        listenerKeyVertical.updateSelectedIndex(selectedMenuIndex)
        stage.draw()
    }

    private fun selectMenuItem() {
        when (selectedMenuIndex) {
            0 -> processFullscreenButton()
            1 -> processMusicButton()
            2 -> processSoundButton()
            3 -> processDebugModeButton()
            4 -> processButton(ScreenType.MENU_SETTINGS, ScreenType.MENU_CONTROLS)
            5 -> processBackButton()
            else -> throw IllegalArgumentException("SelectedIndex not found.")
        }
    }

    private fun processFullscreenButton() {
        preferenceManager.toggleFullscreen()
        fullscreenButton.setText(getMenuItemFullScreen())
    }

    private fun processMusicButton() {
        val mustPlayBgmImmediately = startScreen == ScreenType.MENU_MAIN
        preferenceManager.toggleMusic(mustPlayBgmImmediately)
        musicButton.setText(getMenuItemMusic())
    }

    private fun processSoundButton() {
        preferenceManager.toggleSound()
        soundButton.setText(getMenuItemSound())
    }

    private fun processDebugModeButton() {
        preferenceManager.toggleDebugMode()
        debugModeButton.setText(getMenuItemDebugMode())
    }

    private fun createTable(): Table {
        // styles
        val buttonStyle = TextButtonStyle()
        buttonStyle.font = menuFont
        buttonStyle.fontColor = fontColor

        // actors
        fullscreenButton = TextButton(getMenuItemFullScreen(), TextButtonStyle(buttonStyle))
        musicButton = TextButton(getMenuItemMusic(), TextButtonStyle(buttonStyle))
        soundButton = TextButton(getMenuItemSound(), TextButtonStyle(buttonStyle))
        debugModeButton = TextButton(getMenuItemDebugMode(), TextButtonStyle(buttonStyle))
        val controlsButton = TextButton(MENU_ITEM_CONTROLS, TextButtonStyle(buttonStyle))
        val backButton = TextButton(MENU_ITEM_BACK, TextButtonStyle(buttonStyle))

        // table
        val newTable = Table()
        newTable.setFillParent(true)
        newTable.defaults().center()
        newTable.add(fullscreenButton).row()
        newTable.add(musicButton).row()
        newTable.add(soundButton).row()
        newTable.add(debugModeButton).row()
        newTable.add(controlsButton).row()
        newTable.add(backButton)
        newTable.x = MENU_X
        val logo = stage.actors.peek()
        newTable.top().padTop((logo.height * logo.scaleY) + LOGO_PAD + PAD_TOP)
        return newTable
    }

    private fun getMenuItemFullScreen(): String =
        if (preferenceManager.isFullscreen) MENU_ITEM_FULL_SCREEN_ON else MENU_ITEM_FULL_SCREEN_OFF

    private fun getMenuItemMusic(): String =
        if (preferenceManager.isMusicOn) MENU_ITEM_MUSIC_ON else MENU_ITEM_MUSIC_OFF

    private fun getMenuItemSound(): String =
        if (preferenceManager.isSoundOn) MENU_ITEM_SOUND_ON else MENU_ITEM_SOUND_OFF

    private fun getMenuItemDebugMode(): String =
        if (preferenceManager.isInDebugMode) MENU_ITEM_DEBUG_MODE_ON else MENU_ITEM_DEBUG_MODE_OFF

    private fun applyListeners() {
        listenerKeyVertical = ListenerKeyVertical({ super.updateMenuIndex(it) }, NUMBER_OF_ITEMS)
        val listenerKeyConfirm = ListenerKeyConfirm { selectMenuItem() }
        val listenerKeyCancel = ListenerKeyCancel({ super.updateMenuIndex(it) }, { selectMenuItem() }, EXIT_INDEX)
        table.addListener(listenerKeyVertical)
        table.addListener(listenerKeyConfirm)
        table.addListener(listenerKeyCancel)
    }

}
