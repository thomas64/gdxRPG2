package nl.t64.game.rpg.screens.loot

import com.badlogic.gdx.scenes.scene2d.actions.Actions
import com.badlogic.gdx.scenes.scene2d.ui.Table
import nl.t64.game.rpg.Utils.audioManager
import nl.t64.game.rpg.audio.AudioCommand
import nl.t64.game.rpg.audio.AudioEvent
import nl.t64.game.rpg.components.loot.Loot
import nl.t64.game.rpg.components.party.inventory.InventoryContainer
import nl.t64.game.rpg.components.party.inventory.InventoryDatabase
import nl.t64.game.rpg.components.party.inventory.InventoryGroup
import nl.t64.game.rpg.components.party.inventory.InventoryItem
import nl.t64.game.rpg.screens.inventory.inventoryslot.InventorySlot
import nl.t64.game.rpg.screens.inventory.itemslot.InventoryImage
import nl.t64.game.rpg.screens.inventory.itemslot.ItemSlotSelector
import nl.t64.game.rpg.screens.inventory.tooltip.LootSlotTooltip


private const val SLOT_SIZE = 64f
private const val NUMBER_OF_SLOTS = 8
private const val SLOTS_IN_ROW = 4
private const val INPUT_DELAY = 0.2f

class LootSlotsTable(
    private val lootScreen: LootScreen,
    private val loot: Loot,
    private val tooltip: LootSlotTooltip
) {

    private val inventory = InventoryContainer(NUMBER_OF_SLOTS)
    val lootSlots = Table()
    private val selector = ItemSlotSelector(inventory, lootSlots, SLOTS_IN_ROW)
    private val taker = LootSlotTaker(selector)

    init {
        fillLootInventoryContainer()
        fillLootSlotsTable()
        lootSlots.addAction(Actions.sequence(Actions.delay(INPUT_DELAY),
                                             Actions.run { selectFirstSlot() },
                                             Actions.addListener(createListener(), false)))
    }

    private fun selectFirstSlot() {
        selector.setNewSelectedByIndex(0)
    }

    private fun createListener(): LootSlotsTableListener {
        return LootSlotsTableListener({ closeScreen() },
                                      { toggleTooltip() },
                                      { takeItem() },
                                      { selector.selectNewSlot(it) },
                                      SLOTS_IN_ROW)
    }

    private fun takeItem() {
        if (inventory.isEmpty()) {
            closeScreen()
        } else {
            taker.take(selector.getCurrentSlot())
        }
    }

    private fun closeScreen() {
        if (inventory.isEmpty()) {
            loot.clearContent()
        } else {
            loot.updateContent(inventory.getAllContent())
        }
        lootScreen.closeScreen(inventory.isEmpty())
    }

    private fun toggleTooltip() {
        audioManager.handle(AudioCommand.SE_PLAY_ONCE, AudioEvent.SE_MENU_CONFIRM)
        tooltip.toggle(selector.getCurrentSlot())
    }

    private fun fillLootInventoryContainer() {
        loot.content.entries
            .map { createInventoryItem(it) }
            .forEach { inventory.autoSetItem(it) }
    }

    private fun createInventoryItem(loot: Map.Entry<String, Int>): InventoryItem {
        return InventoryDatabase.createInventoryItem(loot.key, loot.value)
    }

    private fun fillLootSlotsTable() {
        (0 until inventory.getSize()).forEach { createLootSlot(it) }
    }

    private fun createLootSlot(index: Int) {
        val lootSlot = InventorySlot(index, InventoryGroup.LOOT_ITEM, tooltip, inventory)
        inventory.getItemAt(index)?.let {
            lootSlot.addToStack(InventoryImage(it))
        }
        lootSlots.add(lootSlot).size(SLOT_SIZE, SLOT_SIZE)
        if ((index + 1) % SLOTS_IN_ROW == 0) {
            lootSlots.row()
        }
    }

}
