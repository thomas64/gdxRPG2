package nl.t64.game.rpg.screens.world.mapobjects

import com.badlogic.gdx.maps.objects.RectangleMapObject
import com.badlogic.gdx.math.Rectangle
import nl.t64.game.rpg.Utils.brokerManager
import nl.t64.game.rpg.Utils.gameData
import nl.t64.game.rpg.screens.world.entity.Direction
import nl.t64.game.rpg.subjects.CollisionObserver


class GameMapEventDiscover(rectObject: RectangleMapObject) : GameMapObject(rectObject.rectangle), CollisionObserver {

    private val eventId: String = rectObject.name

    init {
        brokerManager.collisionObservers.addObserver(this)
    }

    override fun onNotifyCollision(playerBoundingBox: Rectangle, playerDirection: Direction) {
        if (playerBoundingBox.overlaps(rectangle)) {
            possibleStartEvent()
        }
    }

    private fun possibleStartEvent() {
        val event = gameData.events.getEventById(eventId)
        event.possibleStart()
    }

}
