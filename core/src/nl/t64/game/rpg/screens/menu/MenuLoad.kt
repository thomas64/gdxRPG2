package nl.t64.game.rpg.screens.menu

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.g2d.Sprite
import com.badlogic.gdx.scenes.scene2d.actions.Actions
import com.badlogic.gdx.scenes.scene2d.ui.*
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle
import com.badlogic.gdx.scenes.scene2d.ui.List
import com.badlogic.gdx.scenes.scene2d.ui.List.ListStyle
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle
import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable
import com.badlogic.gdx.utils.Align
import com.badlogic.gdx.utils.Array
import com.badlogic.gdx.utils.ScreenUtils
import nl.t64.game.rpg.Utils.audioManager
import nl.t64.game.rpg.Utils.gameData
import nl.t64.game.rpg.Utils.mapManager
import nl.t64.game.rpg.Utils.profileManager
import nl.t64.game.rpg.Utils.resourceManager
import nl.t64.game.rpg.Utils.screenManager
import nl.t64.game.rpg.audio.AudioCommand
import nl.t64.game.rpg.audio.AudioEvent
import nl.t64.game.rpg.components.cutscene.CutsceneId
import nl.t64.game.rpg.constants.Constant
import nl.t64.game.rpg.constants.ScreenType


private const val SPRITE_TRANSPARENT = "sprites/transparent.png"

private const val TITLE_LABEL = "Select profile"
private const val MENU_ITEM_START = "Start"
private const val MENU_ITEM_LOAD = "Load"
private const val MENU_ITEM_DELETE = "Delete"
private const val MENU_ITEM_BACK = "Back"
private val LOAD_MESSAGE = """
    All progress after the last save will be lost.
    Are you sure you want to load this profile?""".trimIndent()
private val DELETE_MESSAGE = """
    This save file will be removed.
    Are you sure?""".trimIndent()

private const val MENU_X = 604f
private const val TITLE_SPACE_BOTTOM = 10f
private const val BUTTON_SPACE_RIGHT = 20f

private const val NUMBER_OF_ITEMS = 3
private const val DELETE_INDEX = 1
private const val EXIT_INDEX = 2


class MenuLoad : MenuScreen() {

    private lateinit var profiles: Array<String>

    private lateinit var topTable: Table
    private lateinit var listItems: List<String>
    private lateinit var group: VerticalGroup

    private lateinit var listenerKeyVertical: ListenerKeyVertical
    private lateinit var listenerKeyHorizontal: ListenerKeyHorizontal

    private var selectedListIndex = 0

    private var isBgmFading = false
    private var isLoaded = false

    override fun setupScreen() {
        isLoaded = false
        profiles = profileManager.getVisualLoadingArray()

        setFontColor()
        createTables()
        createSomeListeners()

        stage.addActor(topTable)
        stage.addActor(table)
        stage.keyboardFocus = group
        stage.addAction(Actions.alpha(1f))

        super.selectedMenuIndex = 0
        setCurrentTextButtonToSelected()

        Thread { loadProfiles() }.start()
    }

    override fun render(dt: Float) {
        ScreenUtils.clear(Color.BLACK)
        stage.act(dt)
        if (isLoaded) {
            selectedListIndex = listItems.selectedIndex
        }
        listenerKeyVertical.updateSelectedIndex(selectedListIndex)
        listenerKeyHorizontal.updateSelectedIndex(selectedMenuIndex)
        if (isBgmFading) {
            audioManager.fadeBgmBgs()
        }
        stage.draw()
    }

    private fun loadProfiles() {
        profiles = profileManager.getVisualProfileArray()
        listItems.setItems(profiles)
        listItems.selectedIndex = selectedListIndex
        applyAllListeners()
        isLoaded = true
    }

    private fun selectMenuItem() {
        when (selectedMenuIndex) {
            0 -> processLoadButton()
            1 -> processDeleteButton()
            2 -> processBackButton()
            else -> throw  IllegalArgumentException("SelectedIndex not found.")
        }
    }

    private fun processLoadButton() {
        if (profileManager.doesProfileExist(selectedListIndex)) {
            loadGame()
        } else if (startScreen == ScreenType.MENU_MAIN) {
            newGame()
        } else {
            errorSound()
        }
    }

    private fun processDeleteButton() {
        if (profileManager.doesProfileExist(selectedListIndex)) {
            DialogQuestion({ deleteSaveFile() }, DELETE_MESSAGE).show(stage)
        } else {
            errorSound()
        }
    }

    private fun loadGame() {
        if (startScreen == ScreenType.MENU_PAUSE) {
            DialogQuestion({ fadeBeforeOpenWorldScreen() }, LOAD_MESSAGE).show(stage)
        } else {
            fadeBeforeOpenWorldScreen()
        }
    }

    private fun newGame() {
        profileManager.selectedIndex = selectedListIndex
        processButton(ScreenType.MENU_LOAD, ScreenType.MENU_NEW)
    }

    private fun errorSound() {
        audioManager.handle(AudioCommand.SE_STOP, AudioEvent.SE_MENU_CONFIRM)
        audioManager.handle(AudioCommand.SE_PLAY_ONCE, AudioEvent.SE_MENU_ERROR)
    }

    private fun fadeBeforeOpenWorldScreen() {
        Gdx.input.inputProcessor = null
        stage.addAction(Actions.sequence(Actions.run { isBgmFading = true },
                                         Actions.fadeOut(Constant.FADE_DURATION),
                                         Actions.run { isBgmFading = false },
                                         Actions.run { openWorldScreen() }))
    }

    private fun openWorldScreen() {
        mapManager.disposeOldMaps()
        screenManager.getScreen(ScreenType.WORLD) // just load the constructor.
        profileManager.loadProfile(selectedListIndex)
        if (gameData.cutscenes.isPlayed(CutsceneId.SCENE_INTRO)) {
            screenManager.setScreen(ScreenType.WORLD)
        } else {
            gameData.cutscenes.setPlayed(CutsceneId.SCENE_INTRO)
            screenManager.setScreen(ScreenType.SCENE_INTRO)
        }
    }

    private fun deleteSaveFile() {
        profileManager.removeProfile(selectedListIndex)
        profiles = profileManager.getVisualProfileArray()
        listItems.setItems(profiles)
        listItems.selectedIndex = selectedListIndex
    }

    private fun createTables() {
        val spriteTransparent = Sprite(resourceManager.getTextureAsset(SPRITE_TRANSPARENT))
        // styles
        val titleStyle = LabelStyle(menuFont, fontColor)
        val buttonStyle = TextButtonStyle()
        buttonStyle.font = menuFont
        buttonStyle.fontColor = fontColor
        val listStyle = ListStyle()
        listStyle.font = menuFont
        listStyle.fontColorSelected = Constant.DARK_RED
        listStyle.fontColorUnselected = fontColor
        listStyle.background = SpriteDrawable(spriteTransparent)
        listStyle.selection = SpriteDrawable(spriteTransparent)

        // actors
        val titleLabel = Label(TITLE_LABEL, titleStyle)
        val loadButton = TextButton(if (startScreen == ScreenType.MENU_PAUSE) MENU_ITEM_LOAD else MENU_ITEM_START,
                                    TextButtonStyle(buttonStyle))
        val deleteButton = TextButton(MENU_ITEM_DELETE, TextButtonStyle(buttonStyle))
        val backButton = TextButton(MENU_ITEM_BACK, TextButtonStyle(buttonStyle))

        listItems = List(listStyle)
        listItems.setItems(profiles)
        listItems.setAlignment(Align.center)
        group = VerticalGroup()
        group.addActor(listItems)

        // tables
        topTable = Table()
        topTable.setFillParent(true)
        topTable.add(titleLabel).center().spaceBottom(TITLE_SPACE_BOTTOM).row()
        topTable.add(group).center()
        topTable.x = MENU_X
        val logo = stage.actors.peek()
        topTable.top().padTop((logo.height * logo.scaleY) + LOGO_PAD + PAD_TOP)

        // bottom table
        table = Table()
        table.setFillParent(true)
        table.add(loadButton).spaceRight(BUTTON_SPACE_RIGHT)
        table.add(deleteButton).spaceRight(BUTTON_SPACE_RIGHT)
        table.add(backButton)
        table
            .top().padTop(TITLE_SPACE_BOTTOM + topTable.prefHeight)
            .right().padRight(((logo.width * logo.scaleX) / 2f) - (table.prefWidth / 2f) + LOGO_PAD)
    }

    private fun createSomeListeners() {
        listenerKeyVertical = ListenerKeyVertical({ listItems.setSelectedIndex(it) }, listItems.items.size)
        listenerKeyHorizontal = ListenerKeyHorizontal({ super.updateMenuIndex(it) }, NUMBER_OF_ITEMS)
    }

    private fun applyAllListeners() {
        val listenerKeyConfirm = ListenerKeyConfirm { selectMenuItem() }
        val listenerKeyDelete = ListenerKeyDelete({ super.updateMenuIndex(it) }, { selectMenuItem() }, DELETE_INDEX)
        val listenerKeyCancel = ListenerKeyCancel({ super.updateMenuIndex(it) }, { selectMenuItem() }, EXIT_INDEX)
        group.addListener(listenerKeyVertical)
        group.addListener(listenerKeyHorizontal)
        group.addListener(listenerKeyConfirm)
        group.addListener(listenerKeyDelete)
        group.addListener(listenerKeyCancel)
    }

}
