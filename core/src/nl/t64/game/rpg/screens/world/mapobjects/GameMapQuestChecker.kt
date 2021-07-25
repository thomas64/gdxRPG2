package nl.t64.game.rpg.screens.world.mapobjects

import com.badlogic.gdx.maps.objects.RectangleMapObject
import com.badlogic.gdx.math.Rectangle
import com.badlogic.gdx.math.Vector2
import ktx.tiled.property
import nl.t64.game.rpg.Utils.brokerManager
import nl.t64.game.rpg.Utils.gameData
import nl.t64.game.rpg.screens.world.entity.Direction
import nl.t64.game.rpg.subjects.ActionObserver


class GameMapQuestChecker(rectObject: RectangleMapObject) : GameMapObject(rectObject.rectangle), ActionObserver {

    private val questId: String = rectObject.name
    private val taskId: String = rectObject.property("task")

    init {
        brokerManager.actionObservers.addObserver(this)
    }

    override fun onNotifyActionPressed(checkRect: Rectangle, playerDirection: Direction, playerPosition: Vector2) {
        if (checkRect.overlaps(rectangle)) {
            setQuestTaskComplete()
        }
    }

    private fun setQuestTaskComplete() {
        val quest = gameData.quests.getQuestById(questId)
        quest.setTaskComplete(taskId)
    }

}
