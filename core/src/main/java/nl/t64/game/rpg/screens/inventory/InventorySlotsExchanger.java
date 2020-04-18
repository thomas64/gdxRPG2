package nl.t64.game.rpg.screens.inventory;

import lombok.AllArgsConstructor;
import nl.t64.game.rpg.components.party.InventoryGroup;

import java.util.Optional;


@AllArgsConstructor
class InventorySlotsExchanger {

    private final InventoryImage draggedItem;
    private final InventorySlot sourceSlot;
    private final InventorySlot targetSlot;

    void exchange() {
        if (targetSlot.doesAcceptItem(draggedItem)) {
            checkTargetItem();
            InventoryWriter.storeToGameData();
        } else {
            sourceSlot.putItemBack(draggedItem);
        }
    }

    private void checkTargetItem() {
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
            int sourceAmount = sourceSlot.amount + 1;
            InventoryImage targetImage = targetSlot.getCertainInventoryImage();
            int targetAmount = targetSlot.amount;

            sourceSlot.clearStack();
            targetSlot.clearStack();

            sourceSlot.amount = targetAmount;
            sourceSlot.addToStack(targetImage);
            targetSlot.amount = sourceAmount;
            targetSlot.addToStack(sourceImage);
        } else {
            sourceSlot.putItemBack(draggedItem);
        }
    }

    private void putItemInEmptySlot() {
        if (draggedItem.isStackable()
                && sourceSlot.amount >= 1) {
            drag_PartOf_StackToEmptySlot();
        } else {
            targetSlot.putItemInEmptySlot(draggedItem);
        }
    }

    private void drag_PartOf_StackToEmptySlot() {
        if (InventoryUtils.isShiftPressed()) {
            dragAllItemsToEmptySlot();
        } else if (InventoryUtils.isCtrlPressed()) {
            dragHalfOfAmountToEmptySlot();
        } else {
            targetSlot.putItemInEmptySlot(draggedItem);
        }
    }

    private void dragAllItemsToEmptySlot() {
        targetSlot.amount = sourceSlot.amount + 1;
        targetSlot.addToStack(draggedItem);
        sourceSlot.clearStack();
    }

    private void dragHalfOfAmountToEmptySlot() {
        final int half = (int) Math.floor((sourceSlot.amount) / 2f);
        targetSlot.amount = half;
        targetSlot.addToStack(draggedItem);
        targetSlot.incrementAmount();
        sourceSlot.decrementAmountBy(half);
    }

    private void dragAllItemsToFilledSlot() {
        targetSlot.amount = targetSlot.amount + sourceSlot.amount;
        targetSlot.incrementAmount();
        sourceSlot.clearStack();
    }

    private void dragHalfOfAmountToFilledSlot() {
        final int half = (int) Math.floor((sourceSlot.amount) / 2f);
        targetSlot.amount += half;
        targetSlot.incrementAmount();
        sourceSlot.decrementAmountBy(half);
    }

    private boolean doTargetAndSourceAcceptEachOther() {
        return !(sourceSlot.filterGroup.equals(InventoryGroup.SHOP_EQUIP_ITEM)
                || targetSlot.filterGroup.equals(InventoryGroup.SHOP_EQUIP_ITEM))
                && targetSlot.doesAcceptItem(draggedItem)
                && sourceSlot.doesAcceptItem(targetSlot.getCertainInventoryImage());
    }

}
