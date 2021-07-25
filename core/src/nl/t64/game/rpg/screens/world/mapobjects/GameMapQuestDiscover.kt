package nl.t64.game.rpg.screens.world.mapobjects

import com.badlogic.gdx.maps.objects.RectangleMapObject
import com.badlogic.gdx.math.Rectangle
import ktx.tiled.property
import nl.t64.game.rpg.Utils.brokerManager
import nl.t64.game.rpg.Utils.gameData
import nl.t64.game.rpg.screens.world.entity.Direction
import nl.t64.game.rpg.subjects.CollisionObserver


class GameMapQuestDiscover(rectObject: RectangleMapObject) : GameMapObject(rectObject.rectangle), CollisionObserver {

    private val questId: String = rectObject.name
    private val taskId: String = rectObject.property("task")

    init {
        brokerManager.collisionObservers.addObserver(this)
    }

    override fun onNotifyCollision(playerBoundingBox: Rectangle, playerDirection: Direction) {
        if (playerBoundingBox.overlaps(rectangle)) {
            setQuestTaskComplete()
        }
    }

    private fun setQuestTaskComplete() {
        val quest = gameData.quests.getQuestById(questId)
        quest.setTaskComplete(taskId)
    }

}
