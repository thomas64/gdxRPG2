package nl.t64.game.rpg.subjects


class LootSubject {

    private val observers: MutableList<LootObserver> = ArrayList()

    fun addObserver(observer: LootObserver) {
        observers.add(observer)
    }

    fun removeObserver(observer: LootObserver) {
        observers.remove(observer)
    }

    fun removeAllObservers() {
        observers.clear()
    }

    fun notifyLootTaken() {
        observers.forEach { it.onNotifyLootTaken() }
    }

    fun notifyRewardTaken() {
        observers.forEach { it.onNotifyRewardTaken() }
    }

    fun notifyReceiveTaken() {
        observers.forEach { it.onNotifyReceiveTaken() }
    }

}
