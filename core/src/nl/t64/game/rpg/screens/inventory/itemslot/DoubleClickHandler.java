package nl.t64.game.rpg.screens.inventory.itemslot;

import com.badlogic.gdx.scenes.scene2d.utils.DragAndDrop;
import nl.t64.game.rpg.screens.inventory.InventoryUtils;


public final class DoubleClickHandler {

    private DoubleClickHandler() {
        throw new IllegalCallerException("DoubleClickHandler class");
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    public static void handleShop(ItemSlot clickedSlot, DragAndDrop dragAndDrop) {
        clickedSlot.getPossibleInventoryImage()
                   .ifPresent(clickedItem -> exchangeWithPossibleSameStackableItemInventorySlot(clickedItem,
                                                                                                clickedSlot,
                                                                                                dragAndDrop));
    }

    private static void exchangeWithPossibleSameStackableItemInventorySlot(InventoryImage clickedItem,
                                                                           ItemSlot clickedSlot,
                                                                           DragAndDrop dragAndDrop) {
        InventoryUtils.getScreenUI()
                      .getInventorySlotsTable()
                      .getPossibleSameStackableItemSlotWith(clickedItem.inventoryItem)
                      .ifPresentOrElse(targetSlot -> exchangeWithInventorySlot(clickedItem,
                                                                               clickedSlot,
                                                                               targetSlot,
                                                                               dragAndDrop),
                                       () -> exchangeWithPossibleEmptyInventorySlot(clickedItem,
                                                                                    clickedSlot,
                                                                                    dragAndDrop));
    }

    private static void exchangeWithPossibleEmptyInventorySlot(InventoryImage clickedItem,
                                                               ItemSlot clickedSlot,
                                                               DragAndDrop dragAndDrop) {
        InventoryUtils.getScreenUI()
                      .getInventorySlotsTable()
                      .getPossibleEmptySlot()
                      .ifPresent(targetSlot -> exchangeWithInventorySlot(clickedItem,
                                                                         clickedSlot,
                                                                         targetSlot,
                                                                         dragAndDrop));
    }

    private static void exchangeWithInventorySlot(InventoryImage dragImage,
                                                  ItemSlot sourceSlot,
                                                  ItemSlot targetSlot,
                                                  DragAndDrop dragAndDrop) {

        int startAmount = sourceSlot.getAmount();
        sourceSlot.clearStack();
//        if (isHandlingFullStack(startAmount)) {
//            dragImage = createDuplicate(dragImage, startAmount, dragAndDrop);
//        } else if (isHandlingPartOfStack(startAmount)) {
//            duplicateInSource(dragImage, sourceSlot, startAmount, dragAndDrop);
//            int dragAmount = getDragAmount(sourceSlot);
//            dragImage.setAmount(dragAmount);
//            sourceSlot.decrementAmountBy(dragAmount);
//        } else {
//            throw new IllegalStateException("Double click shop cannot reach this.");
//        }
        new ItemSlotsExchanger(dragImage, sourceSlot, targetSlot).exchange();
    }

}
