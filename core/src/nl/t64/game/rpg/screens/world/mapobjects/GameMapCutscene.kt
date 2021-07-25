package nl.t64.game.rpg.screens.world.mapobjects

import com.badlogic.gdx.maps.objects.RectangleMapObject
import com.badlogic.gdx.math.Rectangle
import nl.t64.game.rpg.Utils.brokerManager
import nl.t64.game.rpg.screens.world.entity.Direction
import nl.t64.game.rpg.subjects.CollisionObserver


class GameMapCutscene(rectObject: RectangleMapObject) : GameMapObject(rectObject.rectangle), CollisionObserver {

    private val cutsceneId: String = rectObject.name

    init {
        brokerManager.collisionObservers.addObserver(this)
    }

    override fun onNotifyCollision(playerBoundingBox: Rectangle, playerDirection: Direction) {
        if (playerBoundingBox.overlaps(rectangle)) {
            brokerManager.mapObservers.notifyStartCutscene(cutsceneId)
        }
    }

}
