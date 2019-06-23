package nl.t64.game.rpg.screens.inventory;

import com.badlogic.gdx.utils.GdxRuntimeException;
import lombok.AllArgsConstructor;

import java.util.Optional;
import java.util.function.Consumer;


@AllArgsConstructor
class InventorySlotsExchanger {

    private final InventoryImage draggedItem;
    private final InventorySlot sourceSlot;
    private final InventorySlot targetSlot;

    void exchange() {
        if (draggedItem == null) {
            throw new GdxRuntimeException("");
//            return; // todo, kan de if weg?
        }
        if (targetSlot.doesAcceptItem(draggedItem)) {
            checkTargetItem();
            InventoryWriter.storeToGameData();
        } else {
            sourceSlot.putItemBack(draggedItem);
        }
    }

    private void checkTargetItem() {
        Optional<InventoryImage> possibleItemAtTarget = targetSlot.getPossibleInventoryImage();
        possibleItemAtTarget.ifPresentOrElse(checkForSameItemAndStackable(),
                                             this::putItemInEmptySlot);
    }

    private Consumer<InventoryImage> checkForSameItemAndStackable() {
        return itemAtTarget -> {
            if (InventoryUtils.shiftPressed
                    && draggedItem.isSameItemAs(itemAtTarget)
                    && draggedItem.isStackable()) {
                dragAllItemsToFilledSlot();
            } else if (draggedItem.isSameItemAs(itemAtTarget)
                    && draggedItem.isStackable()) {
                targetSlot.incrementAmount();
            } else {
                swapStacks();
            }
        };
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
        if (InventoryUtils.shiftPressed
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
        return targetSlot.doesAcceptItem(draggedItem) &&
                sourceSlot.doesAcceptItem(targetSlot.getCertainInventoryImage());
    }

}
