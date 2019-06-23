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
            checkTargetItem(draggedItem, sourceSlot);
            InventoryWriter.storeToGameData();
        } else {
            sourceSlot.putItemBack(draggedItem);
        }
    }

    private void checkTargetItem(InventoryImage draggedItem, InventorySlot sourceSlot) {
        Optional<InventoryImage> possibleItemAtTarget = targetSlot.getPossibleInventoryImage();
        possibleItemAtTarget.ifPresentOrElse(checkForSameItemAndStackable(draggedItem, sourceSlot),
                                             () -> targetSlot.putItemInEmptySlot(draggedItem));
    }

    private Consumer<InventoryImage> checkForSameItemAndStackable(InventoryImage draggedItem, InventorySlot sourceSlot) {
        return itemAtTarget -> {
            if (draggedItem.isSameItemAs(itemAtTarget) && draggedItem.isStackable()) {
                targetSlot.incrementAmount();
            } else {
                swapStacks(sourceSlot, targetSlot, draggedItem);
            }
        };
    }

    private void swapStacks(InventorySlot sourceSlot, InventorySlot targetSlot, InventoryImage draggedItem) {
        if (doTargetAndSourceAcceptEachOther(sourceSlot, targetSlot, draggedItem)) {
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

    private boolean doTargetAndSourceAcceptEachOther(InventorySlot sourceSlot,
                                                     InventorySlot targetSlot,
                                                     InventoryImage draggedItem) {
        return targetSlot.doesAcceptItem(draggedItem) &&
                sourceSlot.doesAcceptItem(targetSlot.getCertainInventoryImage());
    }

}
