package nl.t64.game.rpg

import com.badlogic.gdx.Game
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.controllers.Controllers
import nl.t64.game.rpg.audio.AudioManager
import nl.t64.game.rpg.constants.ScreenType
import nl.t64.game.rpg.screens.ScreenManager
import nl.t64.game.rpg.screens.world.MapManager


open class Engine : Game() {

    companion object {
        private var runTime = 0f

        private fun updateRunTime() {
            runTime += Gdx.graphics.deltaTime
        }

        fun getRunTime(): Float {
            return runTime
        }
    }

    lateinit var preferenceManager: PreferenceManager
    val profileManager: ProfileManager = ProfileManager()
    val resourceManager: ResourceManager = ResourceManager()
    val gameData: GameData = GameData()
    val screenManager: ScreenManager = ScreenManager()
    val audioManager: AudioManager = AudioManager()
    val mapManager: MapManager = MapManager()
    val brokerManager: BrokerManager = BrokerManager()
    val gamepadMapping: GamepadMapping = GamepadMapping()

    override fun create() {
        preferenceManager = PreferenceManager()
        preferenceManager.setFullscreenAccordingToPreference()

        brokerManager.profileObservers.addObserver(gameData)
        brokerManager.profileObservers.addObserver(mapManager)

        Controllers.addListener(gamepadMapping.controllerToInputAdapter)

        screenManager.setScreen(ScreenType.MENU_MAIN)
    }

    override fun render() {
        updateRunTime()
        super.render()
    }

    override fun dispose() {
        screenManager.dispose()
    }

}
