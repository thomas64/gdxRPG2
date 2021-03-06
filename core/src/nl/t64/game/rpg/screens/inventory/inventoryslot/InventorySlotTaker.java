package nl.t64.game.rpg.screens.inventory.inventoryslot;

import nl.t64.game.rpg.screens.inventory.InventoryUtils;
import nl.t64.game.rpg.screens.inventory.itemslot.InventoryImage;
import nl.t64.game.rpg.screens.inventory.itemslot.ItemSlot;
import nl.t64.game.rpg.screens.inventory.itemslot.ItemSlotSelector;
import nl.t64.game.rpg.screens.inventory.itemslot.ItemSlotsExchanger;


class InventorySlotTaker {

    private final ItemSlotSelector selector;

    private ItemSlot sourceSlot;
    private InventoryImage candidateItem;

    InventorySlotTaker(ItemSlotSelector selector) {
        this.selector = selector;
    }

    void equip(ItemSlot itemSlot) {
        this.sourceSlot = itemSlot;
        tryPutInventorySlotToEquipSlot();
    }

    private void tryPutInventorySlotToEquipSlot() {
        sourceSlot.getPossibleInventoryImage()
                  .ifPresent(this::tryPutInventorySlotToEquipSlot);
    }

    private void tryPutInventorySlotToEquipSlot(InventoryImage candidateItem) {
        this.candidateItem = candidateItem;
        InventoryUtils.getScreenUI()
                      .getEquipSlotsTables()
                      .getCurrentEquipSlots()
                      .getPossibleSlotOfGroup(candidateItem.inventoryGroup)
                      .ifPresent(this::exchangeWithEquipSlotOfSameInventoryGroup);
    }

    private void exchangeWithEquipSlotOfSameInventoryGroup(ItemSlot targetSlot) {
        sourceSlot.deselect();
        sourceSlot.clearStack();
        new ItemSlotsExchanger(candidateItem, sourceSlot, targetSlot).exchange();
        selector.setNewSelectedByIndex(sourceSlot.index);
    }

}
