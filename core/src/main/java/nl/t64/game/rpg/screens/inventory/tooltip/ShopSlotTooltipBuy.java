package nl.t64.game.rpg.screens.inventory.tooltip;

import nl.t64.game.rpg.components.party.InventoryDescription;

import java.util.List;


public class ShopSlotTooltipBuy extends ItemSlotTooltip {

    @Override
    List<InventoryDescription> removeLeftUnnecessaryAttributes(List<InventoryDescription> descriptionList) {
        descriptionList = removeSell(descriptionList);
        return descriptionList;
    }

    @Override
    List<InventoryDescription> removeRightUnnecessaryAttributes(List<InventoryDescription> descriptionList) {
        descriptionList = removeBuy(descriptionList);
        return descriptionList;
    }

}
