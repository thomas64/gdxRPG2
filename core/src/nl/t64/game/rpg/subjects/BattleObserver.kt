package nl.t64.game.rpg.subjects

import nl.t64.game.rpg.components.loot.Loot


interface BattleObserver {

    fun onNotifyBattleWon(battleId: String, spoils: Loot, levelUpMessage: String?)

}
