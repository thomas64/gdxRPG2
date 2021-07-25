package nl.t64.game.rpg.screens.battle

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Screen
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.scenes.scene2d.actions.Actions
import com.badlogic.gdx.scenes.scene2d.ui.Label
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle
import com.badlogic.gdx.utils.ScreenUtils
import nl.t64.game.rpg.Utils.audioManager
import nl.t64.game.rpg.Utils.resourceManager
import nl.t64.game.rpg.audio.AudioCommand
import nl.t64.game.rpg.audio.AudioEvent
import nl.t64.game.rpg.constants.Constant
import nl.t64.game.rpg.screens.world.Camera


private const val TITLE_FONT = "fonts/spectral_regular_24.ttf"
private const val FONT_SIZE = 24

class BattleScreen : Screen {

    private lateinit var camera: Camera
    private lateinit var stage: Stage

    override fun show() {
        camera = Camera()
        stage = Stage(camera.viewport)
        val battleTitle = createBattleTitle()
        stage.addActor(battleTitle)
        stage.addAction(
            Actions.addAction(
                Actions.sequence(
                    Actions.alpha(0f),
                    Actions.visible(true),
                    Actions.fadeIn(Constant.FADE_DURATION),
                    Actions.run { audioManager.handle(AudioCommand.SE_PLAY_ONCE, AudioEvent.SE_FIGHT_ON) }
                ), battleTitle))
    }

    override fun render(dt: Float) {
        ScreenUtils.clear(Color.BLACK)
        stage.act(dt)
        stage.draw()
    }

    override fun resize(width: Int, height: Int) {
        // empty
    }

    override fun pause() {
        // empty
    }

    override fun resume() {
        // empty
    }

    override fun hide() {
        stage.clear()
    }

    override fun dispose() {
        stage.dispose()
    }

    private fun createBattleTitle(): Label {
        val font = resourceManager.getTrueTypeAsset(TITLE_FONT, FONT_SIZE)
        val style = LabelStyle(font, Color.WHITE)
        val label = Label("Battle...!", style)
        label.setPosition(Gdx.graphics.width / 2f - label.width / 2f, Gdx.graphics.height / 2f - label.height / 2f)
        label.isVisible = false
        return label
    }

}
