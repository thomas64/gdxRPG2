package nl.t64.game.rpg.subjects

import com.badlogic.gdx.graphics.Color
import nl.t64.game.rpg.screens.world.GameMap


interface MapObserver {

    fun onNotifyFadeOut(actionAfterFade: () -> Unit, transitionColor: Color)
    fun onNotifyMapChanged(currentMap: GameMap)
    fun onNotifyShakeCamera()
    fun onNotifyStartCutscene(cutsceneId: String)

}
