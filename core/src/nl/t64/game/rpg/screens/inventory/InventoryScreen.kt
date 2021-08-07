package nl.t64.game.rpg.screens.inventory

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.utils.ScreenUtils
import nl.t64.game.rpg.Utils
import nl.t64.game.rpg.Utils.audioManager
import nl.t64.game.rpg.Utils.brokerManager
import nl.t64.game.rpg.Utils.gameData
import nl.t64.game.rpg.Utils.preferenceManager
import nl.t64.game.rpg.Utils.screenManager
import nl.t64.game.rpg.audio.AudioCommand
import nl.t64.game.rpg.audio.AudioEvent
import nl.t64.game.rpg.components.party.inventory.InventoryDatabase
import nl.t64.game.rpg.constants.ScreenType
import nl.t64.game.rpg.screens.ScreenToLoad
import nl.t64.game.rpg.screens.ScreenUI
import nl.t64.game.rpg.screens.inventory.messagedialog.MessageDialog
import nl.t64.game.rpg.screens.world.conversation.ConversationDialog
import nl.t64.game.rpg.screens.world.conversation.ConversationObserver


class InventoryScreen : ScreenToLoad(), ConversationObserver {

    private val conversationDialog: ConversationDialog = ConversationDialog()
    private lateinit var inventoryUI: InventoryUI

    companion object {
        fun load() {
            audioManager.handle(AudioCommand.SE_PLAY_ONCE, AudioEvent.SE_SCROLL)
            screenManager.openParchmentLoadScreen(ScreenType.INVENTORY)
        }
    }

    init {
        conversationDialog.conversationObservers.addObserver(this)
    }


    override fun onNotifyExitConversation() {
        hideConversationDialog()
    }

    override fun onNotifyHeroDismiss() {
        val selectedHero = InventoryUtils.getSelectedHero()
        gameData.heroes.addHero(selectedHero)
        val heroToDismiss = selectedHero.id
        selectPreviousHero()
        gameData.party.removeHero(heroToDismiss)
        hideConversationDialog()
        brokerManager.partyObservers.notifyHeroDismissed()
    }

    override fun getScreenUI(): ScreenUI {
        return inventoryUI
    }

    override fun show() {
        Gdx.input.inputProcessor = stage
        Utils.setGamepadInputProcessor(stage)
        stage.addListener(InventoryScreenListener({ closeScreen() },
                                                  { doAction() },
                                                  { selectPreviousHero() },
                                                  { selectNextHero() },
                                                  { selectPreviousTable() },
                                                  { selectNextTable() },
                                                  { tryToDismissHero() },
                                                  { sortInventory() },
                                                  { toggleTooltip() },
                                                  { toggleCompare() },
                                                  { cheatAddGold() },
                                                  { cheatRemoveGold() }))
        inventoryUI = InventoryUI.create(stage)
        ButtonLabels(stage).create()
    }

    override fun render(dt: Float) {
        ScreenUtils.clear(Color.BLACK)
        stage.act(dt)
        stage.draw()
        inventoryUI.update()
        conversationDialog.update(dt)
    }

    override fun hide() {
        inventoryUI.unloadAssets()
        stage.clear()
    }

    override fun dispose() {
        stage.dispose()
        conversationDialog.dispose()
    }

    private fun closeScreen() {
        Gdx.input.inputProcessor = null
        Utils.setGamepadInputProcessor(null)
        audioManager.handle(AudioCommand.SE_PLAY_ONCE, AudioEvent.SE_SCROLL)
        fadeParchment()
    }

    private fun doAction() {
        inventoryUI.doAction()
    }

    private fun selectPreviousHero() {
        audioManager.handle(AudioCommand.SE_PLAY_ONCE, AudioEvent.SE_MENU_CURSOR)
        inventoryUI.updateSelectedHero { InventoryUtils.selectPreviousHero() }
    }

    private fun selectNextHero() {
        audioManager.handle(AudioCommand.SE_PLAY_ONCE, AudioEvent.SE_MENU_CURSOR)
        inventoryUI.updateSelectedHero { InventoryUtils.selectNextHero() }
    }

    private fun selectPreviousTable() {
        audioManager.handle(AudioCommand.SE_PLAY_ONCE, AudioEvent.SE_MENU_CURSOR)
        inventoryUI.selectPreviousTable()
    }

    private fun selectNextTable() {
        audioManager.handle(AudioCommand.SE_PLAY_ONCE, AudioEvent.SE_MENU_CURSOR)
        inventoryUI.selectNextTable()
    }

    private fun tryToDismissHero() {
        val party = gameData.party
        val currentHero = InventoryUtils.getSelectedHero()
        val currentHeroId = currentHero.id
        if (party.isPlayer(currentHeroId)) {
            val errorMessage = "You cannot dismiss the party leader."
            val messageDialog = MessageDialog(errorMessage)
            messageDialog.show(stage, AudioEvent.SE_MENU_ERROR)
        } else {
            conversationDialog.loadConversation("dismiss_$currentHeroId", currentHeroId)
            conversationDialog.show()
        }
    }

    private fun hideConversationDialog() {
        conversationDialog.hideWithFade()
        Gdx.input.inputProcessor = stage
        Utils.setGamepadInputProcessor(stage)
    }

    private fun sortInventory() {
        audioManager.handle(AudioCommand.SE_PLAY_ONCE, AudioEvent.SE_MENU_CONFIRM)
        gameData.inventory.sort()
        inventoryUI.reloadInventory()
    }

    private fun toggleTooltip() {
        audioManager.handle(AudioCommand.SE_PLAY_ONCE, AudioEvent.SE_MENU_CONFIRM)
        inventoryUI.toggleTooltip()
    }

    private fun toggleCompare() {
        audioManager.handle(AudioCommand.SE_PLAY_ONCE, AudioEvent.SE_MENU_CONFIRM)
        inventoryUI.toggleCompare()
    }

    private fun cheatAddGold() {
        if (preferenceManager.isInDebugMode) {
            val inventory = gameData.inventory
            if (inventory.hasRoomForResource("gold")
                && inventory.hasRoomForResource("herb")
                && inventory.hasRoomForResource("spice")
                && inventory.hasRoomForResource("gemstone")
            ) {
                audioManager.handle(AudioCommand.SE_PLAY_ONCE, AudioEvent.SE_MENU_ERROR)
                inventory.autoSetItem(InventoryDatabase.createInventoryItem("gold", 100))
                inventory.autoSetItem(InventoryDatabase.createInventoryItem("herb", 100))
                inventory.autoSetItem(InventoryDatabase.createInventoryItem("spice", 100))
                inventory.autoSetItem(InventoryDatabase.createInventoryItem("gemstone", 100))
                inventoryUI.reloadInventory()
            }
        }
    }

    private fun cheatRemoveGold() {
        if (preferenceManager.isInDebugMode) {
            val inventory = gameData.inventory
            if (inventory.hasEnoughOfItem("gold", 1)) {
                audioManager.handle(AudioCommand.SE_PLAY_ONCE, AudioEvent.SE_MENU_ERROR)
                inventory.autoRemoveItem("gold", 1)
                inventoryUI.reloadInventory()
            }
        }
    }

}
