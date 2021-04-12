package nl.t64.game.rpg.screens.inventory.tooltip;

import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Align;
import nl.t64.game.rpg.Utils;
import nl.t64.game.rpg.components.party.InventoryDescription;
import nl.t64.game.rpg.components.party.InventoryGroup;
import nl.t64.game.rpg.components.party.SkillItemId;
import nl.t64.game.rpg.screens.inventory.itemslot.InventoryImage;
import nl.t64.game.rpg.screens.inventory.itemslot.ItemSlot;

import java.util.List;


public class LootSlotTooltip extends ItemSlotTooltip {

    @Override
    void updateDescription(ItemSlot itemSlot) {
        window.clear();
        if (itemSlot.hasItem()) {
            InventoryImage hoveredImage = itemSlot.getCertainInventoryImage();
            InventoryGroup inventoryGroup = hoveredImage.inventoryItem.getGroup();

            if (inventoryGroup.equals(InventoryGroup.RESOURCE)
                || inventoryGroup.equals(InventoryGroup.POTION)
                || inventoryGroup.equals(InventoryGroup.ITEM)) {
                createResourceTooltip(hoveredImage);
            } else {
                createSingleTooltip(hoveredImage);
            }
        }
        window.pack();
    }

    @Override
    void createSingleTooltip(InventoryImage inventoryImage) {
        final var hoveredTable = new Table();
        hoveredTable.defaults().align(Align.left);

        final int totalMerchant = Utils.getGameData().getParty().getSumOfSkill(SkillItemId.MERCHANT);
        List<InventoryDescription> descriptionList = inventoryImage.getComparelessDescription(totalMerchant);
        removeLeftUnnecessaryAttributes(descriptionList);
        descriptionList.forEach(attribute -> addToTable(hoveredTable, attribute, createSingleLabelStyle(attribute)));
        addPossibleDescription(inventoryImage, hoveredTable);
        window.add(hoveredTable);
    }

}
