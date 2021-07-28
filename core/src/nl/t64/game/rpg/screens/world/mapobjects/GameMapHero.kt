package nl.t64.game.rpg.screens.world.mapobjects

import com.badlogic.gdx.maps.objects.RectangleMapObject
import ktx.tiled.property


class GameMapHero(rectObject: RectangleMapObject) : GameMapNpc(rectObject) {

    val hasBeenRecruited: Boolean = rectObject.property("hasBeenRecruited", false)

}
