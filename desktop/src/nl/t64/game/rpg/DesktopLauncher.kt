package nl.t64.game.rpg

import com.badlogic.gdx.Files.FileType
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration
import nl.t64.game.rpg.constants.Constant


class DesktopLauncher {

    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            val config = Lwjgl3ApplicationConfiguration()
            config.setTitle(Constant.TITLE)
            config.setWindowIcon("sprites/icon.png")
            config.setResizable(false)
            config.setWindowedMode(Constant.SCREEN_WIDTH, Constant.SCREEN_HEIGHT)
            config.setPreferencesConfig("T64.nl/", FileType.External)

            Lwjgl3Application(Engine(), config)
        }
    }

}
