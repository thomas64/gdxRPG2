package nl.t64.game.rpg.screens.shop;

import com.badlogic.gdx.graphics.Color;
import nl.t64.game.rpg.GameData;
import nl.t64.game.rpg.Utils;
import nl.t64.game.rpg.components.party.InventoryContainer;
import nl.t64.game.rpg.components.party.InventoryGroup;
import nl.t64.game.rpg.components.party.SkillItemId;
import nl.t64.game.rpg.screens.inventory.inventoryslot.InventorySlot;
import nl.t64.game.rpg.screens.inventory.itemslot.InventoryImage;
import nl.t64.game.rpg.screens.inventory.tooltip.ItemSlotTooltip;


class ShopSlot extends InventorySlot {

    ShopSlot(int index, ItemSlotTooltip tooltip, InventoryContainer inventory) {
        super(index, InventoryGroup.SHOP_ITEM, tooltip, inventory);
    }

    void refreshPurchaseColor() {
        getPossibleInventoryImage().ifPresent(this::refreshPurchaseColor);
    }

    private void refreshPurchaseColor(InventoryImage image) {
        GameData gameData = Utils.getGameData();
        int totalMerchant = gameData.getParty().getSumOfSkill(SkillItemId.MERCHANT);
        int costsToBuy = image.inventoryItem.getBuyPricePiece(totalMerchant);
        if (gameData.getInventory().hasEnoughOfItem("gold", costsToBuy)) {
            image.setColor(Color.WHITE);
        } else {
            image.setColor(Color.DARK_GRAY);
        }
    }

}
