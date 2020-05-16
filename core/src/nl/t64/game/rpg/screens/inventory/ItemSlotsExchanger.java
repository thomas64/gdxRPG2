package nl.t64.game.rpg.screens.inventory;

import nl.t64.game.rpg.Utils;
import nl.t64.game.rpg.components.party.InventoryDatabase;
import nl.t64.game.rpg.components.party.InventoryGroup;
import nl.t64.game.rpg.components.party.InventoryItem;
import nl.t64.game.rpg.components.party.SkillItemId;

import java.util.Optional;


class ItemSlotsExchanger {

    private final InventoryImage draggedItem;
    private final ItemSlot sourceSlot;
    private final ItemSlot targetSlot;
    private boolean isSuccessfullyExchanged;

    ItemSlotsExchanger(InventoryImage draggedItem, ItemSlot sourceSlot, ItemSlot targetSlot) {
        this.draggedItem = draggedItem;
        this.sourceSlot = sourceSlot;
        this.targetSlot = targetSlot;
        this.isSuccessfullyExchanged = true;
    }

    void exchange() {
        if (targetSlot.doesAcceptItem(draggedItem)) {
            handleExchange();
        } else {
            sourceSlot.putItemBack(draggedItem);
        }
    }

    private void handleExchange() {
        if (isShopPurchase()) {
            handlePurchase();
        } else if (isShopBarter()) {
            handleBarter();
        } else {
            handlePossibleExchange();
        }
    }

    private void handlePurchase() {
        final int totalMerchant = Utils.getGameData().getParty().getSumOfSkill(SkillItemId.MERCHANT);
        final int totalPrice = draggedItem.inventoryItem.getBuyPrice(totalMerchant) * getAmountOfDraggedItems();
        if (Utils.getGameData().getInventory().hasEnoughOfResource("gold", totalPrice)) {
            handlePossibleExchange();
            if (isSuccessfullyExchanged) {
                InventoryUtils.getScreenUI().getInventorySlotsTable().removeResource("gold", totalPrice);
            }
        } else {
            new MessageDialog("I'm sorry. You don't seem to have enough gold.").show(sourceSlot.getStage());
            sourceSlot.putItemBack(draggedItem);
        }
    }

    private void handleBarter() {
        final int totalMerchant = Utils.getGameData().getParty().getSumOfSkill(SkillItemId.MERCHANT);
        final int totalValue = draggedItem.inventoryItem.getSellValue(totalMerchant) * getAmountOfDraggedItems();
        if (totalValue == 0) {
            new MessageDialog("I'm sorry. I can't accept that.").show(sourceSlot.getStage());
            sourceSlot.putItemBack(draggedItem);
            return;
        }

        if (Utils.getGameData().getInventory().hasRoomForResource("gold")) {
            handlePossibleExchange();
            if (isSuccessfullyExchanged) {
                InventoryItem gold = InventoryDatabase.getInstance().createInventoryItem("gold", totalValue);
                InventoryUtils.getScreenUI().getInventorySlotsTable().addResource(gold);
            }
        } else {
            new MessageDialog("I'm sorry. You don't seem to have room for gold.").show(sourceSlot.getStage());
            sourceSlot.putItemBack(draggedItem);
        }
    }

    private void handlePossibleExchange() {
        Optional<InventoryImage> possibleItemAtTarget = targetSlot.getPossibleInventoryImage();
        possibleItemAtTarget.ifPresentOrElse(this::putItemInFilledSlot,
                                             this::putItemInEmptySlot);
    }

    private void putItemInFilledSlot(InventoryImage itemAtTarget) {
        if (targetSlot.equals(sourceSlot)) {
            targetSlot.incrementAmount();
        } else if (draggedItem.isSameItemAs(itemAtTarget)
                   && draggedItem.isStackable()) {
            drag_PartOf_StackToFilledSlot();
        } else {
            swapStacks();
        }
    }

    private void drag_PartOf_StackToFilledSlot() {
        if (InventoryUtils.isShiftPressed()) {
            dragAllItemsToFilledSlot();
        } else if (InventoryUtils.isCtrlPressed()) {
            dragHalfOfAmountToFilledSlot();
        } else {
            targetSlot.incrementAmount();
        }
    }

    private void swapStacks() {
        if (doTargetAndSourceAcceptEachOther()) {
            InventoryImage sourceImage = sourceSlot.getPossibleInventoryImage().orElse(draggedItem);
            InventoryImage targetImage = targetSlot.getCertainInventoryImage();

            final boolean thereWasMoreThanOneItemAtSource = sourceSlot.getAmount() > 0;
            sourceSlot.clearStack();
            targetSlot.clearStack();

            sourceSlot.addToStack(targetImage);
            targetSlot.addToStack(sourceImage);
            if (thereWasMoreThanOneItemAtSource) {
                targetSlot.incrementAmount();
            }
        } else {
            sourceSlot.putItemBack(draggedItem);
            isSuccessfullyExchanged = false;
        }
    }

    private void putItemInEmptySlot() {
        if (draggedItem.isStackable()
            && sourceSlot.getAmount() >= 1) {
            drag_PartOf_StackToEmptySlot();
        } else {
            targetSlot.putSingleItemInEmptySlot(draggedItem);
        }
    }

    private void drag_PartOf_StackToEmptySlot() {
        if (InventoryUtils.isShiftPressed()) {
            dragAllItemsToEmptySlot();
        } else if (InventoryUtils.isCtrlPressed()) {
            dragHalfOfAmountToEmptySlot();
        } else {
            targetSlot.putSingleItemInEmptySlot(draggedItem);
        }
    }

    private void dragAllItemsToEmptySlot() {
        targetSlot.addToStack(draggedItem);
        targetSlot.incrementAmountBy(sourceSlot.getAmount());
        sourceSlot.clearStack();
    }

    private void dragHalfOfAmountToEmptySlot() {
        final int half = getHalfOfSource();
        targetSlot.addToStack(draggedItem);
        targetSlot.incrementAmountBy(half);
        sourceSlot.decrementAmountBy(half);
    }

    private void dragAllItemsToFilledSlot() {
        targetSlot.incrementAmountBy(sourceSlot.getAmount() + 1);
        sourceSlot.clearStack();
    }

    private void dragHalfOfAmountToFilledSlot() {
        final int half = getHalfOfSource();
        targetSlot.incrementAmountBy(half + 1);
        sourceSlot.decrementAmountBy(half);
    }

    private int getAmountOfDraggedItems() {
        if (InventoryUtils.isShiftPressed()) {
            return sourceSlot.getAmount() + 1;
        } else if (InventoryUtils.isCtrlPressed()) {
            return getHalfOfSource() + 1;
        } else {
            return 1;
        }
    }

    private boolean isShopPurchase() {
        return sourceSlot.filterGroup.equals(InventoryGroup.SHOP_ITEM)
               && !targetSlot.filterGroup.equals(InventoryGroup.SHOP_ITEM);
    }

    private boolean isShopBarter() {
        return !sourceSlot.filterGroup.equals(InventoryGroup.SHOP_ITEM)
               && targetSlot.filterGroup.equals(InventoryGroup.SHOP_ITEM);
    }

    private int getHalfOfSource() {
        return (int) Math.floor((sourceSlot.getAmount()) / 2f);
    }

    private boolean doTargetAndSourceAcceptEachOther() {
        return !(sourceSlot.filterGroup.equals(InventoryGroup.SHOP_ITEM)
                 || targetSlot.filterGroup.equals(InventoryGroup.SHOP_ITEM))
               && targetSlot.doesAcceptItem(draggedItem)
               && sourceSlot.doesAcceptItem(targetSlot.getCertainInventoryImage());
    }

}
