package nl.t64.game.rpg.screens.inventory.equipslot;

import nl.t64.game.rpg.audio.AudioEvent;
import nl.t64.game.rpg.screens.inventory.InventoryUtils;
import nl.t64.game.rpg.screens.inventory.itemslot.InventoryImage;
import nl.t64.game.rpg.screens.inventory.itemslot.ItemSlot;
import nl.t64.game.rpg.screens.inventory.itemslot.ItemSlotsExchanger;
import nl.t64.game.rpg.screens.inventory.messagedialog.MessageDialog;


class EquipSlotTaker {

    private final EquipSlotSelector selector;

    private ItemSlot sourceSlot;
    private InventoryImage candidateItem;
    private ItemSlot targetSlot;

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
        this.targetSlot = targetSlot;
        InventoryUtils.getSelectedHero().createMessageIfNotAbleToDequip(candidateItem.inventoryItem)
                      .ifPresentOrElse(this::showErrorMessage, this::exchange);
    }

    private void showErrorMessage(String message) {
        new MessageDialog(message).show(sourceSlot.getStage(), AudioEvent.SE_MENU_ERROR);
    }

    private void exchange() {
        sourceSlot.deselect();
        sourceSlot.clearStack();
        new ItemSlotsExchanger(candidateItem, sourceSlot, targetSlot).exchange();
        selector.setNewSelected(sourceSlot);
    }

}
