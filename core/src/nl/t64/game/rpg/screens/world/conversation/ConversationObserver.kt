package nl.t64.game.rpg.screens.world.conversation

import nl.t64.game.rpg.components.loot.Loot


interface ConversationObserver {

    fun onNotifyExitConversation()

    fun onNotifyShowMessageTooltip(message: String) {
        throw IllegalCallerException("Implement this method in child.")
    }

    fun onNotifyShowLevelUpDialog(message: String) {
        throw IllegalCallerException("Implement this method in child.")
    }

    fun onNotifyLoadShop() {
        throw IllegalCallerException("Implement this method in child.")
    }

    fun onNotifyShowRewardDialog(reward: Loot, levelUpMessage: String?) {
        throw IllegalCallerException("Implement this method in child.")
    }

    fun onNotifyShowReceiveDialog(receive: Loot) {
        throw IllegalCallerException("Implement this method in child.")
    }

    fun onNotifyHeroJoined() {
        throw IllegalCallerException("Implement this method in child.")
    }

    fun onNotifyHeroDismiss() {
        throw IllegalCallerException("Implement this method in child.")
    }

}
