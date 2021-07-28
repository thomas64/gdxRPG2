package nl.t64.game.rpg.screens.world.mapobjects

import com.badlogic.gdx.maps.objects.RectangleMapObject
import com.badlogic.gdx.math.Rectangle
import com.badlogic.gdx.math.Vector2
import ktx.tiled.property
import nl.t64.game.rpg.Utils.brokerManager
import nl.t64.game.rpg.Utils.gameData
import nl.t64.game.rpg.Utils.mapManager
import nl.t64.game.rpg.subjects.BlockObserver


class GameMapQuestBlocker(rectObject: RectangleMapObject) : GameMapObject(rectObject.rectangle), BlockObserver {

    private val questId: String = rectObject.name
    private val isActiveIfComplete: Boolean = rectObject.property("activeIfComplete")
    private val taskId: String = rectObject.property("task")
    private var isActive: Boolean = rectObject.property<Boolean>("isActive")
        .also { isActive -> if (isActive) brokerManager.blockObservers.addObserver(this) }

    override fun getBlockerFor(boundingBox: Rectangle): Rectangle? {
        return rectangle.takeIf { isActive && boundingBox.overlaps(rectangle) }
    }

    override fun isBlocking(point: Vector2): Boolean {
        return isActive && rectangle.contains(point)
    }

    fun update() {
        val isFinished = gameData.quests.getQuestById(questId).isFinished()
        val isComplete = gameData.quests.getQuestById(questId).isTaskComplete(taskId)

        val before = isActive
        isActive = (isFinished || isComplete) == isActiveIfComplete
        val after = isActive
        if (before != after) {
            changeBlocker()
        }
    }

    private fun changeBlocker() {
        if (isActive) {
            brokerManager.blockObservers.addObserver(this)
        } else {
            brokerManager.blockObservers.removeObserver(this)
        }
        mapManager.setTiledGraph()
    }

}
