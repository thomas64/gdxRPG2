package nl.t64.game.rpg.subjects


interface LootObserver {

    fun onNotifyLootTaken()
    fun onNotifyRewardTaken()
    fun onNotifyReceiveTaken()

}
