package nl.t64.game.rpg.screens.inventory.equipslot;

import nl.t64.game.rpg.screens.inventory.InventoryUtils;
import nl.t64.game.rpg.screens.inventory.itemslot.InventoryImage;
import nl.t64.game.rpg.screens.inventory.itemslot.ItemSlot;
import nl.t64.game.rpg.screens.inventory.itemslot.ItemSlotsExchanger;


class EquipSlotTaker {

    private final EquipSlotSelector selector;

    private ItemSlot sourceSlot;
    private InventoryImage candidateItem;

    EquipSlotTaker(EquipSlotSelector selector) {
        this.selector = selector;
    }

    void dequip(ItemSlot equipSlot) {
        this.sourceSlot = equipSlot;
        tryPutEquipSlotToInventorySlot();
    }

    private void tryPutEquipSlotToInventorySlot() {
        sourceSlot.getPossibleInventoryImage()
                  .ifPresent(this::tryPutEquipSlotToInventorySlot);
    }

    private void tryPutEquipSlotToInventorySlot(InventoryImage candidateItem) {
        this.candidateItem = candidateItem;
        InventoryUtils.getScreenUI()
                      .getInventorySlotsTable()
                      .getPossibleEmptySlot()
                      .ifPresent(this::exchangeWithEmptyInventorySlot);
    }

    private void exchangeWithEmptyInventorySlot(ItemSlot targetSlot) {
        sourceSlot.deselect();
        sourceSlot.clearStack();
        new ItemSlotsExchanger(candidateItem, sourceSlot, targetSlot).exchange();
        selector.setNewSelected(sourceSlot);
    }

}
