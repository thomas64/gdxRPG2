package nl.t64.game.rpg.screens.inventory.tooltip

import nl.t64.game.rpg.components.party.inventory.InventoryDescription


class ShopSlotTooltipSell : ItemSlotTooltip() {

    override fun removeLeftUnnecessaryAttributes(descriptionList: MutableList<InventoryDescription>) {
        removeBuy(descriptionList)
    }

}
