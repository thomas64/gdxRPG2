package nl.t64.game.rpg.subjects

import nl.t64.game.rpg.components.loot.Loot


class BattleSubject {

    private val observers: MutableList<BattleObserver> = ArrayList()

    fun addObserver(observer: BattleObserver) {
        observers.add(observer)
    }

    fun removeObserver(observer: BattleObserver) {
        observers.remove(observer)
    }

    fun removeAllObservers() {
        observers.clear()
    }

    fun notifyBattleWon(battleId: String, spoils: Loot, levelUpMessage: String?) {
        observers.forEach { it.onNotifyBattleWon(battleId, spoils, levelUpMessage) }
    }

}
