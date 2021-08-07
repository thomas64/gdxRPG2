package nl.t64.game.rpg.screens.questlog

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.scenes.scene2d.ui.Label
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle
import com.badlogic.gdx.utils.ScreenUtils
import nl.t64.game.rpg.Utils
import nl.t64.game.rpg.Utils.audioManager
import nl.t64.game.rpg.Utils.screenManager
import nl.t64.game.rpg.audio.AudioCommand
import nl.t64.game.rpg.audio.AudioEvent
import nl.t64.game.rpg.constants.ScreenType
import nl.t64.game.rpg.screens.ScreenToLoad


private const val QUESTS_WINDOW_POSITION_X = 63f
private const val QUESTS_WINDOW_POSITION_Y = 50f
private const val SUMMARY_WINDOW_POSITION_X = 18f
private const val SUMMARY_WINDOW_POSITION_Y = 834f
private const val TASKS_WINDOW_POSITION_X = 18f
private const val TASKS_WINDOW_POSITION_Y = 50f
private const val LABEL_PADDING_BOTTOM = 26f

class QuestLogScreen : ScreenToLoad() {

    companion object {
        fun load() {
            audioManager.handle(AudioCommand.SE_PLAY_ONCE, AudioEvent.SE_SCROLL)
            screenManager.openParchmentLoadScreen(ScreenType.QUEST_LOG)
        }
    }

    override fun show() {
        Gdx.input.inputProcessor = stage
        Utils.setGamepadInputProcessor(stage)
        stage.addListener(QuestLogScreenListener { closeScreen() })

        val questLogUI = QuestLogUI().apply {
            questListWindow.setPosition(QUESTS_WINDOW_POSITION_X, QUESTS_WINDOW_POSITION_Y)
            summaryWindow.setPosition(Gdx.graphics.width / 2f + SUMMARY_WINDOW_POSITION_X, SUMMARY_WINDOW_POSITION_Y)
            taskListWindow.setPosition(Gdx.graphics.width / 2f + TASKS_WINDOW_POSITION_X, TASKS_WINDOW_POSITION_Y)
            addToStage(stage)
            applyListeners()
        }

        val buttonLabel = Label(createText(), LabelStyle(BitmapFont(), Color.BLACK))
        buttonLabel.setPosition((QUESTS_WINDOW_POSITION_X + (questLogUI.questListWindow.width / 2f))
                                        - (buttonLabel.width / 2f), LABEL_PADDING_BOTTOM)
        stage.addActor(buttonLabel)
        stage.keyboardFocus = questLogUI.questListTable.questList
        stage.scrollFocus = questLogUI.questListTable.scrollPane
    }

    override fun render(dt: Float) {
        ScreenUtils.clear(Color.BLACK)
        stage.act(dt)
        stage.draw()
    }

    override fun hide() {
        stage.clear()
    }

    override fun dispose() {
        stage.dispose()
    }

    private fun closeScreen() {
        Gdx.input.inputProcessor = null
        Utils.setGamepadInputProcessor(null)
        audioManager.handle(AudioCommand.SE_PLAY_ONCE, AudioEvent.SE_SCROLL)
        fadeParchment()
    }

    private fun createText(): String {
        return if (Utils.isGamepadConnected()) {
            "[L1] Page-up      [R1] Page-down      [B] Back"
        } else {
            "[Left] Page-up      [Right] Page-down      [L] Back"
        }
    }

}
