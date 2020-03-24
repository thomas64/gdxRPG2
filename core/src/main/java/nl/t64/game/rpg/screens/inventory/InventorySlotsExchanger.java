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
        possibleItemAtTarget.ifPresentOrElse(this::checkForSameItemAndStackable,
                                             this::putItemInEmptySlot);
    }

    private void checkForSameItemAndStackable(InventoryImage itemAtTarget) {
        if (InventoryUtils.isShiftPressed()
            && draggedItem.isSameItemAs(itemAtTarget)
            && draggedItem.isStackable()) {
            dragAllItemsToFilledSlot();
        } else if (draggedItem.isSameItemAs(itemAtTarget)
                   && draggedItem.isStackable()) {
            targetSlot.incrementAmount();
        } else {
            swapStacks();
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
        if (InventoryUtils.isShiftPressed()
            && draggedItem.isStackable()
            && sourceSlot.amount >= 1) {
            dragAllItemsToEmptySlot();
        } else {
            targetSlot.putItemInEmptySlot(draggedItem);
        }
    }

    private void dragAllItemsToEmptySlot() {
        targetSlot.amount = sourceSlot.amount + 1;
        targetSlot.addToStack(draggedItem);
        sourceSlot.clearStack();
    }

    private void dragAllItemsToFilledSlot() {
        targetSlot.amount = targetSlot.amount + sourceSlot.amount;
        targetSlot.incrementAmount();
        sourceSlot.clearStack();
    }

    private boolean doTargetAndSourceAcceptEachOther() {
        return !(sourceSlot.filterGroup.equals(InventoryGroup.SHOP_EQUIP_ITEM)
                 || targetSlot.filterGroup.equals(InventoryGroup.SHOP_EQUIP_ITEM))
               && targetSlot.doesAcceptItem(draggedItem)
               && sourceSlot.doesAcceptItem(targetSlot.getCertainInventoryImage());
    }

}
