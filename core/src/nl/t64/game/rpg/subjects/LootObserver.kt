package nl.t64.game.rpg.subjects


interface LootObserver {

    fun onNotifySpoilsUpdated()
    fun onNotifyLootTaken()
    fun onNotifyRewardTaken()
    fun onNotifyReceiveTaken()

}
