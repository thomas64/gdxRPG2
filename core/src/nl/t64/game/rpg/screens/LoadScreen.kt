package nl.t64.game.rpg.screens

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Screen
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.scenes.scene2d.actions.Actions
import com.badlogic.gdx.scenes.scene2d.ui.Image
import com.badlogic.gdx.utils.ScreenUtils
import nl.t64.game.rpg.Utils
import nl.t64.game.rpg.Utils.screenManager
import nl.t64.game.rpg.constants.Constant
import nl.t64.game.rpg.constants.ScreenType


class LoadScreen : Screen {

    private val stage: Stage = Stage()
    lateinit var screenTypeToLoad: ScreenType
    private var isLoaded: Boolean = false

    override fun show() {
        stage.addActor(Utils.createScreenshot(true))
        val parchment = createParchment()
        stage.addActor(parchment)
        parchment.addAction(Actions.sequence(Actions.alpha(0f),
                                             Actions.fadeIn(Constant.FADE_DURATION),
                                             Actions.run { isLoaded = true }))
        Gdx.input.inputProcessor = stage
    }

    override fun render(dt: Float) {
        ScreenUtils.clear(Color.BLACK)
        stage.act(dt)
        stage.draw()
        if (isLoaded) {
            setScreenThatJustLoaded()
        }
    }

    private fun setScreenThatJustLoaded() {
        val screenToLoad = screenManager.getScreen(screenTypeToLoad) as ScreenToLoad
        screenToLoad.setBackground((stage.actors[0] as Image),
                                   (stage.actors[1] as Image))
        screenManager.setScreen(screenTypeToLoad)
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
        isLoaded = false
        stage.clear()
        Gdx.input.inputProcessor = null
    }

    override fun dispose() {
        stage.dispose()
    }

    private fun createParchment(): Image {
        return if (hasSmallParchment()) {
            Utils.createSmallParchment()
        } else {
            Utils.createLargeParchment()
        }
    }

    private fun hasSmallParchment(): Boolean {
        return when (screenTypeToLoad) {
            ScreenType.FIND, ScreenType.REWARD, ScreenType.RECEIVE, ScreenType.SPOILS -> true
            else -> false
        }
    }

}
