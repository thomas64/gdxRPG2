package nl.t64.game.rpg.components.tooltip;

import nl.t64.game.rpg.components.party.InventoryDescription;

import java.util.List;


public class ShopSlotTooltipBuy extends InventorySlotTooltip {

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
