package nl.t64.game.rpg.subjects

import com.badlogic.gdx.graphics.Color
import nl.t64.game.rpg.screens.world.GameMap


class MapSubject {

    private val observers: MutableList<MapObserver> = ArrayList()

    fun addObserver(observer: MapObserver) {
        observers.add(observer)
    }

    fun removeObserver(observer: MapObserver) {
        observers.remove(observer)
    }

    fun removeAllObservers() {
        observers.clear()
    }

    fun notifyMapWillChange(changeMap: Runnable, transitionColor: Color) {
        observers.forEach { it.onNotifyMapWillChange(changeMap, transitionColor) }
    }

    fun notifyMapChanged(currentMap: GameMap) {
        observers.forEach { it.onNotifyMapChanged(currentMap) }
    }

    fun notifyShakeCamera() {
        observers.forEach { it.onNotifyShakeCamera() }
    }

}
