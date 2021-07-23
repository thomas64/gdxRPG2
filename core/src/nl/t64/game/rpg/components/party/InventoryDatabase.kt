package nl.t64.game.rpg.components.party

import com.badlogic.gdx.Gdx
import nl.t64.game.rpg.Utils.readValue


private const val INVENTORY_CONFIGS = "configs/inventory/"
private const val FILE_LIST = INVENTORY_CONFIGS + "_files.txt"
private const val DEFAULT_SHOP_RESOURCE_AMOUNT = 100
private const val DEFAULT_SHOP_POTION_AMOUNT = 20

object InventoryDatabase {

    private val inventoryItems: Map<String, InventoryItem> = Gdx.files.internal(FILE_LIST).readString()
        .split(System.lineSeparator())
        .map { Gdx.files.internal(INVENTORY_CONFIGS + it).readString() }
        .map { readValue(it, InventoryItem::class.java) }
        .flatMap { it.toList() }
        .toMap()
        .onEach { (inventoryItemId: String, inventoryItem: InventoryItem) -> inventoryItem.id = inventoryItemId }

    @JvmStatic
    fun createInventoryItemForShop(itemId: String): InventoryItem {
        val inventoryItem = createInventoryItem(itemId)
        if (inventoryItem.group == InventoryGroup.RESOURCE) {
            inventoryItem.amount = DEFAULT_SHOP_RESOURCE_AMOUNT
        } else if (inventoryItem.group == InventoryGroup.POTION) {
            inventoryItem.amount = DEFAULT_SHOP_POTION_AMOUNT
        }
        return inventoryItem
    }

    @JvmStatic
    fun createInventoryItem(itemId: String): InventoryItem {
        return createInventoryItem(itemId, 1)
    }

    @JvmStatic
    fun createInventoryItem(itemId: String, amount: Int): InventoryItem {
        val inventoryItem = inventoryItems[itemId]
        return InventoryItem(inventoryItem, amount)
    }

}
