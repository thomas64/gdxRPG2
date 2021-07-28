package nl.t64.game.rpg.screens.world.mapobjects

import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.maps.objects.RectangleMapObject
import com.badlogic.gdx.math.Rectangle
import com.badlogic.gdx.math.Vector2
import nl.t64.game.rpg.Utils.brokerManager
import nl.t64.game.rpg.Utils.mapManager
import nl.t64.game.rpg.screens.world.entity.Direction
import nl.t64.game.rpg.subjects.ActionObserver


class GameMapWarpPoint(rectObject: RectangleMapObject, fromMapName: String) : GameMapRelocator(
    rectObject.rectangle,
    fromMapName,
    rectObject.name,
    createToMapLocation(rectObject),
    Color.WHITE
), ActionObserver {

    init {
        brokerManager.actionObservers.addObserver(this)
    }

    override fun onNotifyActionPressed(checkRect: Rectangle, playerDirection: Direction, playerPosition: Vector2) {
        if (checkRect.overlaps(rectangle)) {
            mapManager.checkWarpPoint(this, playerDirection)
        }
    }

}
