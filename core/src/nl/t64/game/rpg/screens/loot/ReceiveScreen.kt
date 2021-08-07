package nl.t64.game.rpg.screens.loot

import nl.t64.game.rpg.Utils.audioManager
import nl.t64.game.rpg.Utils.brokerManager
import nl.t64.game.rpg.Utils.screenManager
import nl.t64.game.rpg.audio.AudioCommand
import nl.t64.game.rpg.audio.AudioEvent
import nl.t64.game.rpg.components.loot.Loot
import nl.t64.game.rpg.constants.ScreenType


class ReceiveScreen : LootScreen() {

    companion object {
        fun load(receive: Loot) {
            audioManager.handle(AudioCommand.SE_PLAY_ONCE, AudioEvent.SE_REWARD)
            val receiveScreen = screenManager.getScreen(ScreenType.RECEIVE) as ReceiveScreen
            receiveScreen.loot = receive
            receiveScreen.lootTitle = "   Receive"
            screenManager.openParchmentLoadScreen(ScreenType.RECEIVE)
        }
    }

    override fun resolveAfterClearingContent() {
        brokerManager.lootObservers.notifyReceiveTaken()
    }

}
