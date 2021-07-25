package nl.t64.game.rpg.screens.world.mapobjects

import com.badlogic.gdx.maps.objects.TextureMapObject
import ktx.tiled.property
import nl.t64.game.rpg.Utils.gameData


class GameMapQuestTexture(val texture: TextureMapObject) {

    private val questId: String = texture.name
    private val isVisibleIfComplete: Boolean = texture.property("visibleIfComplete")
    private val taskId: String = texture.property("task")
    var isVisible: Boolean = texture.property("isVisible")

    fun update() {
        val isFinished = gameData.quests.getQuestById(questId).isFinished()
        val isComplete = gameData.quests.getQuestById(questId).isTaskComplete(taskId)
        isVisible = (isFinished || isComplete) == isVisibleIfComplete
    }

}
