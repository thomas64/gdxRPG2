package nl.t64.game.rpg.screens.inventory.inventoryslot;

import nl.t64.game.rpg.screens.inventory.InventoryUtils;
import nl.t64.game.rpg.screens.inventory.itemslot.InventoryImage;
import nl.t64.game.rpg.screens.inventory.itemslot.ItemSlot;
import nl.t64.game.rpg.screens.inventory.itemslot.ItemSlotSelector;
import nl.t64.game.rpg.screens.inventory.itemslot.ItemSlotsExchanger;


class InventorySlotTaker {

    private final ItemSlotSelector selector;
    private Choice choice;
    private ItemSlot sourceSlot;
    private InventoryImage candidateItem;

    InventorySlotTaker(ItemSlotSelector selector) {
        this.selector = selector;
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

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

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    void sellOne(ItemSlot itemSlot) {
        this.choice = Choice.ONE;
        tryPutInventorySlotToShopSlot(itemSlot);
    }

    void sellHalf(ItemSlot itemSlot) {
        this.choice = Choice.HALF;
        tryPutInventorySlotToShopSlot(itemSlot);
    }

    void sellFull(ItemSlot itemSlot) {
        this.choice = Choice.FULL;
        tryPutInventorySlotToShopSlot(itemSlot);
    }

    private void tryPutInventorySlotToShopSlot(ItemSlot sourceSlot) {
        this.sourceSlot = sourceSlot;
        sourceSlot.getPossibleInventoryImage()
                  .ifPresent(this::tryPutInventorySlotToShopSlot);
    }

    private void tryPutInventorySlotToShopSlot(InventoryImage candidateItem) {
        this.candidateItem = candidateItem;
        InventoryUtils.getScreenUI()
                      .getShopSlotsTable()
                      .getPossibleSameStackableItemSlotWith(candidateItem.inventoryItem)
                      .ifPresentOrElse(this::exchangeWithShopSlot,
                                       this::exchangeWithPossibleEmptyShopSlot);
    }

    private void exchangeWithPossibleEmptyShopSlot() {
        InventoryUtils.getScreenUI()
                      .getShopSlotsTable()
                      .getPossibleEmptySlot()
                      .ifPresent(this::exchangeWithShopSlot);
    }

    private void exchangeWithShopSlot(ItemSlot targetSlot) {
        sourceSlot.deselect();
        int takeAmount = getAndTakeAmount();
        new ItemSlotsExchanger(candidateItem, takeAmount, sourceSlot, targetSlot).exchange();
        selector.setNewSelectedByIndex(sourceSlot.index);
    }

    private int getAndTakeAmount() {
        int startAmount = sourceSlot.getAmount();
        return switch (choice) {
            case ONE -> getAndTakeOne(startAmount);
            case HALF -> getAndTakeHalf(startAmount);
            case FULL -> getAndTakeFull(startAmount);
        };
    }

    private int getAndTakeOne(int startAmount) {
        if (startAmount == 1) {
            return getAndTakeFull(startAmount);
        } else {
            sourceSlot.decrementAmountBy(1);
            return 1;
        }
    }

    private int getAndTakeHalf(int startAmount) {
        if (startAmount == 1) {
            return getAndTakeFull(startAmount);
        } else {
            int takeAmount = sourceSlot.getHalfOfAmount();
            sourceSlot.decrementAmountBy(takeAmount);
            return takeAmount;
        }
    }

    private int getAndTakeFull(int startAmount) {
        sourceSlot.clearStack();
        return startAmount;
    }

    private enum Choice {
        ONE, HALF, FULL;
    }

}
