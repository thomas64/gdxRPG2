package nl.t64.game.rpg

import com.badlogic.gdx.Application
import com.badlogic.gdx.Files
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.backends.lwjgl.LwjglApplication
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration
import nl.t64.game.rpg.Utils.preferenceManager
import nl.t64.game.rpg.constants.Constant


fun main() {
    val config = LwjglApplicationConfiguration()
    config.title = Constant.TITLE
    config.width = Constant.SCREEN_WIDTH
    config.height = Constant.SCREEN_HEIGHT
    config.addIcon("sprites/icon.png", Files.FileType.Internal)
    config.resizable = false
    config.preferencesDirectory = "T64.nl/"

    Gdx.app = LwjglApplication(Engine(), config)
    Gdx.app.postRunnable { preferenceManager.setFullscreenAccordingToPreference() }
    Gdx.app.logLevel = Application.LOG_ERROR
}
