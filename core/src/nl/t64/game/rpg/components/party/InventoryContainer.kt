package nl.t64.game.rpg.components.party

import java.util.*


class InventoryContainer(numberOfSlots: Int = NUMBER_OF_SLOTS) {

    companion object {
        private const val NUMBER_OF_SLOTS = 66
        private const val SORTING_SPLIT = 70000 // atm, item.json starts with this number.
    }

    private val inventory: MutableList<InventoryItem?> = arrayOfNulls<InventoryItem>(numberOfSlots).toMutableList()

    fun getAllContent(): Map<String, Int> {
        return inventory.filterNotNull().associate { Pair(it.id, it.amount) }.toMutableMap()
    }

    fun getAllFilledSlots(): List<InventoryItem> {
        return inventory.filterNotNull().toList()
    }

    fun getAmountOfItemAt(index: Int): Int {
        return inventory[index]?.amount ?: 0
    }

    fun incrementAmountAt(index: Int, amount: Int) {
        inventory[index]?.increaseAmountWith(amount)
            ?: throw IllegalStateException("There is no item to increment amount.")
    }

    fun decrementAmountAt(index: Int, amount: Int) {
        inventory[index]?.decreaseAmountWith(amount)
            ?: throw IllegalStateException("There is no item to decrement amount.")
    }

    fun getItemAt(index: Int): Optional<InventoryItem> {
        return Optional.ofNullable(inventory[index]);
    }

    fun autoSetItem(newItem: InventoryItem) {
        if (newItem.isStackable) {
            addResource(newItem)
        } else {
            addItemAtEmptySlot(newItem)
        }
    }

    fun autoRemoveItem(itemId: String, orgAmount: Int) {
        check(hasEnoughOfItem(itemId, orgAmount)) { "Cannot remove this resource from Inventory." }

        var amount = orgAmount
        for ((key, value) in findAllSlotsWithAmountOfItem(itemId)) {
            if (amount == 0) {
                break
            } else if (amount >= value) {
                amount -= value
                clearItemAt(key)
            } else {
                decrementAmountAt(key, amount)
                amount = 0
            }
        }
    }

    fun clearItemAt(index: Int) {
        forceSetItemAt(index, null)
    }

    fun forceSetItemAt(index: Int, newItem: InventoryItem?) {
        inventory[index] = newItem
    }

    fun getSize(): Int {
        return inventory.size
    }

    fun isEmpty(): Boolean {
        return inventory.all { it == null }
    }

    fun sort() {
        InventoryStacksMerger(this).searchAll()
        val sortedList = inventory.sortedWith(compareBy(this::getSort, this::getInventoryGroup))
        inventory.clear()
        inventory.addAll(sortedList)
    }

    fun contains(items: Map<String, Int>): Boolean {
        return if (items.isEmpty()) {
            false
        } else {
            items.entries.all { hasEnoughOfItem(it.key, it.value) }
        }
    }

    fun hasEnoughOfItem(itemId: String, amount: Int): Boolean {
        return getTotalOfItem(itemId) >= amount
    }

    fun hasEmptySlot(): Boolean {
        return findFirstEmptySlot2() != null
    }

    fun hasRoomForResource(itemId: String): Boolean {
        return (findFirstSlotWithItem2(itemId) != null
                || findFirstEmptySlot2() != null)
    }

    fun findFirstSlotWithItem(itemId: String): Optional<Int> {
        return Optional.ofNullable(findFirstSlotWithItem2(itemId))
    }

    private fun findFirstSlotWithItem2(itemId: String): Int? {
        return (0 until getSize()).firstOrNull { containsItemAt(it, itemId) }
    }

    fun findFirstFilledSlot(): Optional<Int> {
        return findNextFilledSlotFrom(0)
    }

    fun findNextFilledSlotFrom(index: Int): Optional<Int> {
        return Optional.ofNullable((index until getSize()).firstOrNull { isSlotFilled(it) })
    }

    fun findFirstEmptySlot(): Optional<Int> {
        return Optional.ofNullable(findFirstEmptySlot2())
    }

    private fun findFirstEmptySlot2(): Int? {
        return (0 until getSize()).firstOrNull { isSlotEmpty(it) }
    }

    fun getLastIndex(): Int {
        return getSize() - 1
    }

    fun contains(itemId: String): Boolean {
        return inventory.filterNotNull().any { it.hasSameIdAs(itemId) }
    }

    fun getTotalOfItem(itemId: String): Int {
        return inventory
            .filterNotNull()
            .filter { it.hasSameIdAs(itemId) }
            .sumOf { it.amount }
    }

    fun getNumberOfFilledSlots(): Int {
        return inventory.filterNotNull().count()
    }

    private fun addResource(newItem: InventoryItem) {
        getItem(newItem.id)?.increaseAmountWith(newItem) ?: addItemAtEmptySlot(newItem)
    }

    private fun addItemAtEmptySlot(newItem: InventoryItem) {
        findFirstEmptySlot2()?.let { slotIsEmptySoSetItemAt(it, newItem) }
            ?: throw IllegalStateException("Inventory is full.")
    }

    private fun slotIsEmptySoSetItemAt(index: Int, newItem: InventoryItem) {
        forceSetItemAt(index, newItem)
    }

    private fun getItem(itemId: String): InventoryItem? {
        return inventory.filterNotNull().firstOrNull { it.hasSameIdAs(itemId) }
    }

    private fun findAllSlotsWithAmountOfItem(itemId: String): Map<Int, Int> {
        val resourceMap: MutableMap<Int, Int> = HashMap()
        (0 until getSize()).forEach { possibleAddToAmountToResourceMap(it, resourceMap, itemId) }
        return resourceMap
    }

    private fun possibleAddToAmountToResourceMap(index: Int, resourceMap: MutableMap<Int, Int>, itemId: String) {
        inventory[index]?.takeIf { it.id == itemId }?.let { resourceMap.put(index, it.amount) }
    }

    private fun isSlotFilled(index: Int): Boolean {
        return !isSlotEmpty(index)
    }

    private fun isSlotEmpty(index: Int): Boolean {
        return inventory[index] == null
    }

    private fun containsItemAt(index: Int, itemId: String): Boolean {
        return inventory[index]?.hasSameIdAs(itemId) ?: false
    }

    private fun getInventoryGroup(inventoryItem: InventoryItem?): InventoryGroup {
        return inventoryItem?.group ?: InventoryGroup.EMPTY
    }

    private fun getSort(inventoryItem: InventoryItem?): Int {
        return inventoryItem?.sort ?: SORTING_SPLIT
    }

}
