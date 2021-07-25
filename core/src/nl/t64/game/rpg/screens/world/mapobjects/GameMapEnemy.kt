package nl.t64.game.rpg.screens.world.mapobjects

import com.badlogic.gdx.maps.objects.RectangleMapObject


class GameMapEnemy(rectObject: RectangleMapObject) : GameMapNpc(rectObject) {

    val battleId: String = createBattle(rectObject)

    private fun createBattle(rectObject: RectangleMapObject): String {
        return rectObject.properties.get("battleId", String::class.java)
    }

}