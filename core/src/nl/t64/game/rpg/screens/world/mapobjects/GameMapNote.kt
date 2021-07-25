package nl.t64.game.rpg.screens.world.mapobjects

import com.badlogic.gdx.maps.objects.RectangleMapObject
import com.badlogic.gdx.math.Rectangle
import com.badlogic.gdx.math.Vector2
import nl.t64.game.rpg.Utils.brokerManager
import nl.t64.game.rpg.screens.world.entity.Direction
import nl.t64.game.rpg.subjects.ActionObserver


class GameMapNote(rectObject: RectangleMapObject) : GameMapObject(rectObject.rectangle), ActionObserver {

    private val noteId: String = rectObject.name

    init {
        brokerManager.actionObservers.addObserver(this)
    }

    override fun onNotifyActionPressed(checkRect: Rectangle, playerDirection: Direction, playerPosition: Vector2) {
        if (checkRect.overlaps(rectangle)) {
            brokerManager.componentObservers.notifyShowNoteDialog(noteId)
        }
    }

}
