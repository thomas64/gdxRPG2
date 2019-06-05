package nl.t64.game.rpg.screens.inventory;

import com.badlogic.gdx.scenes.scene2d.utils.DragAndDrop.Payload;
import com.badlogic.gdx.scenes.scene2d.utils.DragAndDrop.Source;
import com.badlogic.gdx.scenes.scene2d.utils.DragAndDrop.Target;
import com.badlogic.gdx.utils.GdxRuntimeException;

import java.util.Optional;
import java.util.function.Consumer;


class InventorySlotTarget extends Target {

    private InventorySlot targetSlot;

    InventorySlotTarget(InventorySlot targetSlot) {
        super(targetSlot);
        this.targetSlot = targetSlot;
    }

    @Override
    public boolean drag(Source source, Payload payload, float x, float y, int pointer) {
        return true;
    }

    @Override
    public void drop(Source source, Payload payload, float x, float y, int pointer) {
        InventoryImage draggedItem = (InventoryImage) payload.getDragActor();
        InventorySlot sourceSlot = ((InventorySlotSource) source).sourceSlot;

        if (draggedItem == null) {
            throw new GdxRuntimeException("");
//            return; // todo, kan de if weg?
        }
        if (targetSlot.doesAcceptInventoryGroup(draggedItem.inventoryGroup)) {
            checkTargetItem(draggedItem, sourceSlot);
        } else {
            sourceSlot.putItemBack(draggedItem);
        }
    }

    private void checkTargetItem(InventoryImage draggedItem, InventorySlot sourceSlot) {
        Optional<InventoryImage> possibleItemAtTarget = targetSlot.getPossibleInventoryImage();
        possibleItemAtTarget.ifPresentOrElse(
                checkForSameItemAndStackable(draggedItem, sourceSlot),
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

    private boolean doTargetAndSourceAcceptEachOther(InventorySlot sourceSlot, InventorySlot targetSlot, InventoryImage draggedItem) {
        return targetSlot.doesAcceptInventoryGroup(draggedItem.inventoryGroup) &&
                sourceSlot.doesAcceptInventoryGroup(targetSlot.getCertainInventoryImage().inventoryGroup);
    }

}
