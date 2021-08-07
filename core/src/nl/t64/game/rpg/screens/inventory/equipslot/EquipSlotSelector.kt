package nl.t64.game.rpg.screens.inventory.equipslot

import nl.t64.game.rpg.Utils.audioManager
import nl.t64.game.rpg.audio.AudioCommand
import nl.t64.game.rpg.audio.AudioEvent
import nl.t64.game.rpg.screens.inventory.itemslot.ItemSlot


internal class EquipSlotSelector(
    private val equipSlotList: List<ItemSlot>
) {
    private lateinit var itemSlot: ItemSlot

    fun getCurrentSlot(): ItemSlot =
        itemSlot

    fun deselectCurrentSlot() {
        itemSlot.deselect()
    }

    fun selectCurrentSlot() {
        itemSlot.select()
    }

    fun getIndex(): Int =
        equipSlotList.indexOf(itemSlot)

    fun setNewCurrentByIndex(index: Int) {
        itemSlot = equipSlotList[index]
    }

    fun trySelectNewSlot(newIndex: Int) {
        findItemSlotWithIndex(itemSlot.index + newIndex)?.let {
            selectNewSlot(it)
        } ?: trySelectNewSlot2(newIndex)
    }

    private fun trySelectNewSlot2(newIndex: Int) {
        val newSelectedIndex = when {
            newIndex > 0 -> itemSlot.index + newIndex + SIDE_STEP
            else -> itemSlot.index + newIndex - SIDE_STEP
        }
        findItemSlotWithIndex(newSelectedIndex)?.let {
            selectNewSlot(it)
        }
    }

    private fun selectNewSlot(newSlot: ItemSlot) {
        audioManager.handle(AudioCommand.SE_PLAY_ONCE, AudioEvent.SE_MENU_CURSOR)
        itemSlot.deselect()
        setNewSelected(newSlot)
    }

    private fun findItemSlotWithIndex(newSelectedIndex: Int): ItemSlot? {
        return equipSlotList.firstOrNull { it.index == newSelectedIndex }
    }

    fun setNewSelected(newSlot: ItemSlot) {
        itemSlot = newSlot
        itemSlot.select()
    }

}
