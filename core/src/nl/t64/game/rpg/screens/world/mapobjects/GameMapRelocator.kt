package nl.t64.game.rpg.screens.world.mapobjects

import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.maps.objects.RectangleMapObject
import com.badlogic.gdx.math.Rectangle
import ktx.tiled.type
import nl.t64.game.rpg.screens.world.entity.Direction


abstract class GameMapRelocator(
    rectangle: Rectangle,
    val fromMapName: String,
    val toMapName: String,
    val toMapLocation: String,
    val fadeColor: Color
) : GameMapObject(rectangle) {

    lateinit var enterDirection: Direction

    companion object {
        fun createToMapLocation(rectObject: RectangleMapObject): String {
            return rectObject.type ?: ""
        }
    }

}
