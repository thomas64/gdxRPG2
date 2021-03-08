package nl.t64.game.rpg.screens.shop;

import nl.t64.game.rpg.screens.inventory.InventoryUtils;
import nl.t64.game.rpg.screens.inventory.itemslot.InventoryImage;
import nl.t64.game.rpg.screens.inventory.itemslot.ItemSlot;
import nl.t64.game.rpg.screens.inventory.itemslot.ItemSlotSelector;
import nl.t64.game.rpg.screens.inventory.itemslot.ItemSlotsExchanger;


class ShopSlotTaker {

    private final ItemSlotSelector selector;
    private Choice choice;
    private ItemSlot sourceSlot;
    private InventoryImage candidateItem;

    ShopSlotTaker(ItemSlotSelector selector) {
        this.selector = selector;
    }

    void buyOne(ItemSlot itemSlot) {
        this.choice = Choice.ONE;
        tryPutShopSlotToInventorySlot(itemSlot);
    }

    void buyHalf(ItemSlot itemSlot) {
        this.choice = Choice.HALF;
        tryPutShopSlotToInventorySlot(itemSlot);
    }

    void buyFull(ItemSlot itemSlot) {
        this.choice = Choice.FULL;
        tryPutShopSlotToInventorySlot(itemSlot);
    }

    private void tryPutShopSlotToInventorySlot(ItemSlot sourceSlot) {
        this.sourceSlot = sourceSlot;
        sourceSlot.getPossibleInventoryImage()
                  .ifPresent(this::tryPutShopSlotToInventorySlot);
    }

    private void tryPutShopSlotToInventorySlot(InventoryImage candidateItem) {
        this.candidateItem = candidateItem;
        InventoryUtils.getScreenUI()
                      .getInventorySlotsTable()
                      .getPossibleSameStackableItemSlotWith(candidateItem.inventoryItem)
                      .ifPresentOrElse(this::exchangeWithInventorySlot,
                                       this::exchangeWithPossibleEmptyInventorySlot);
    }

    private void exchangeWithPossibleEmptyInventorySlot() {
        InventoryUtils.getScreenUI()
                      .getInventorySlotsTable()
                      .getPossibleEmptySlot()
                      .ifPresent(this::exchangeWithInventorySlot);
    }

    private void exchangeWithInventorySlot(ItemSlot targetSlot) {
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
