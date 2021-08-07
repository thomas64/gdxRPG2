package nl.t64.game.rpg.screens.shop

import com.badlogic.gdx.graphics.Color
import nl.t64.game.rpg.Utils.gameData
import nl.t64.game.rpg.components.party.inventory.InventoryContainer
import nl.t64.game.rpg.components.party.inventory.InventoryGroup
import nl.t64.game.rpg.components.party.skills.SkillItemId
import nl.t64.game.rpg.screens.inventory.inventoryslot.InventorySlot
import nl.t64.game.rpg.screens.inventory.itemslot.InventoryImage
import nl.t64.game.rpg.screens.inventory.tooltip.ItemSlotTooltip


internal class ShopSlot(index: Int, tooltip: ItemSlotTooltip, inventory: InventoryContainer
) : InventorySlot(index, InventoryGroup.SHOP_ITEM, tooltip, inventory) {

    fun refreshPurchaseColor() {
        getPossibleInventoryImage()?.let { refreshPurchaseColor(it) }
    }

    private fun refreshPurchaseColor(image: InventoryImage) {
        val totalMerchant = gameData.party.getSumOfSkill(SkillItemId.MERCHANT)
        val costsToBuy = image.inventoryItem.getBuyPricePiece(totalMerchant)
        if (gameData.inventory.hasEnoughOfItem("gold", costsToBuy)) {
            image.color = Color.WHITE
        } else {
            image.color = Color.DARK_GRAY
        }
    }

}
