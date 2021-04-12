package nl.t64.game.rpg.screens.inventory.tooltip;

import nl.t64.game.rpg.components.party.InventoryDescription;

import java.util.List;


public class ShopSlotTooltipBuy extends ItemSlotTooltip {

    @Override
    void removeLeftUnnecessaryAttributes(List<InventoryDescription> descriptionList) {
        removeSell(descriptionList);
    }

    @Override
    void removeRightUnnecessaryAttributes(List<InventoryDescription> descriptionList) {
        removeBuy(descriptionList);
    }

}
