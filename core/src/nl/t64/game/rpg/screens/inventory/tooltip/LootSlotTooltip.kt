package nl.t64.game.rpg.screens.inventory.tooltip

import com.badlogic.gdx.scenes.scene2d.ui.Table
import com.badlogic.gdx.utils.Align
import nl.t64.game.rpg.Utils.gameData
import nl.t64.game.rpg.components.party.inventory.InventoryGroup
import nl.t64.game.rpg.components.party.skills.SkillItemId
import nl.t64.game.rpg.screens.inventory.itemslot.InventoryImage
import nl.t64.game.rpg.screens.inventory.itemslot.ItemSlot


class LootSlotTooltip : ItemSlotTooltip() {

    override fun updateDescription(itemSlot: ItemSlot) {
        window.clear()
        if (itemSlot.hasItem()) {
            val hoveredImage = itemSlot.getCertainInventoryImage()
            val inventoryGroup = hoveredImage.inventoryItem.group

            if (inventoryGroup == InventoryGroup.RESOURCE
                || inventoryGroup == InventoryGroup.POTION
                || inventoryGroup == InventoryGroup.ITEM
            ) {
                createResourceTooltip(hoveredImage)
            } else {
                createSingleTooltip(hoveredImage)
            }
        }
        window.pack()
    }

    override fun createSingleTooltip(inventoryImage: InventoryImage) {
        val hoveredTable = Table()
        hoveredTable.defaults().align(Align.left)

        val totalMerchant = gameData.party.getSumOfSkill(SkillItemId.MERCHANT)
        val descriptionList = inventoryImage.getComparelessDescription(totalMerchant).toMutableList()
        removeLeftUnnecessaryAttributes(descriptionList)
        descriptionList.forEach { addToTable(hoveredTable, it, createSingleLabelStyle(it)) }
        addPossibleDescription(inventoryImage, hoveredTable)
        window.add(hoveredTable)
    }

}
