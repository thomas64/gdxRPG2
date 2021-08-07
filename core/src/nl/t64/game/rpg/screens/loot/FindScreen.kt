package nl.t64.game.rpg.screens.loot

import nl.t64.game.rpg.Utils.audioManager
import nl.t64.game.rpg.Utils.brokerManager
import nl.t64.game.rpg.Utils.screenManager
import nl.t64.game.rpg.audio.AudioCommand
import nl.t64.game.rpg.audio.AudioEvent
import nl.t64.game.rpg.components.loot.Loot
import nl.t64.game.rpg.constants.ScreenType


class FindScreen : LootScreen() {

    companion object {
        fun load(loot: Loot, event: AudioEvent) {
            audioManager.handle(AudioCommand.SE_PLAY_ONCE, event)
            val findScreen = screenManager.getScreen(ScreenType.FIND) as FindScreen
            findScreen.loot = loot
            findScreen.lootTitle = "   Found"
            screenManager.openParchmentLoadScreen(ScreenType.FIND)
        }
    }

    override fun resolveAfterClearingContent() {
        brokerManager.lootObservers.notifyLootTaken()
    }

}
