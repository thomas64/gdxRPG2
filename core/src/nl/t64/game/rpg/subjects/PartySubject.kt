package nl.t64.game.rpg.subjects


class PartySubject {

    private val observers: MutableList<PartyObserver> = ArrayList()

    fun addObserver(observer: PartyObserver) {
        observers.add(observer)
    }

    fun removeObserver(observer: PartyObserver) {
        observers.remove(observer)
    }

    fun removeAllObservers() {
        observers.clear()
    }

    fun notifyHeroDismissed() {
        observers.forEach { it.onNotifyHeroDismissed() }
    }

}
