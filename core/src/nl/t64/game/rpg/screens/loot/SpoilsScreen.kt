package nl.t64.game.rpg.screens.loot

import com.badlogic.gdx.utils.Null
import nl.t64.game.rpg.Utils.audioManager
import nl.t64.game.rpg.Utils.brokerManager
import nl.t64.game.rpg.Utils.screenManager
import nl.t64.game.rpg.audio.AudioCommand
import nl.t64.game.rpg.audio.AudioEvent
import nl.t64.game.rpg.components.loot.Loot
import nl.t64.game.rpg.constants.ScreenType
import nl.t64.game.rpg.screens.inventory.messagedialog.MessageDialog


class SpoilsScreen : LootScreen() {

    @Null
    private var levelUpMessage: String? = null

    companion object {
        fun load(spoils: Loot, @Null levelUpMessage: String?) {
            audioManager.handle(AudioCommand.SE_PLAY_ONCE, AudioEvent.SE_SPARKLE)
            val spoilsScreen = screenManager.getScreen(ScreenType.SPOILS) as SpoilsScreen
            spoilsScreen.loot = spoils
            spoilsScreen.lootTitle = "   Loot"
            spoilsScreen.levelUpMessage = levelUpMessage
            screenManager.openParchmentLoadScreen(ScreenType.SPOILS)
        }
    }

    override fun show() {
        super.show()
        levelUpMessage?.let { showLevelUpMessage() }
    }

    override fun closeScreen(isAllTheLootCleared: Boolean) {
        brokerManager.lootObservers.notifySpoilsUpdated()
        super.closeScreen(false)
        // Above argument is false so that resolveAfterClearingContent() is not called in closeScreen(),
        // which is empty anyway and also already resolved at the line with notifySpoilsUpdated().
        // This is necessary, so that loot is always updated, even if the spoils are not taken.
    }

    override fun resolveAfterClearingContent() {
        // empty
    }

    private fun showLevelUpMessage() {
        val messageDialog = MessageDialog(levelUpMessage!!)
        messageDialog.setLeftAlignment()
        messageDialog.show(stage, AudioEvent.SE_LEVELUP)
    }

}
