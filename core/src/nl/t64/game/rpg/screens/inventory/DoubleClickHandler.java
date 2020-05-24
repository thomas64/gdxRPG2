package nl.t64.game.rpg.screens.inventory;

import com.badlogic.gdx.scenes.scene2d.utils.DragAndDrop;
import nl.t64.game.rpg.components.party.InventoryItem;

import java.util.Map;


public final class DoubleClickHandler {

    private DoubleClickHandler() {
        throw new IllegalStateException("DoubleClickHandler class");
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    static void handleInventory(ItemSlot clickedSlot, DragAndDrop dragAndDrop) {
        clickedSlot.getPossibleInventoryImage()
                   .ifPresent(clickedItem -> exchangeWithPossibleEquipSlotOfGroup(clickedItem, clickedSlot));
    }

    private static void exchangeWithPossibleEquipSlotOfGroup(InventoryImage clickedItem, ItemSlot clickedSlot) {
        Map<String, EquipSlotsTable> equipSlotsTables = InventoryUtils.getScreenUI().getEquipSlotsTables();
        if (!equipSlotsTables.isEmpty()) {
            equipSlotsTables.get(InventoryUtils.getSelectedHero().getId())
                            .getPossibleSlotOfGroup(clickedItem.inventoryGroup)
                            .ifPresent(targetSlot -> exchangeWithEquipSlotOfGroup(clickedItem,
                                                                                  clickedSlot,
                                                                                  targetSlot));
        }
    }

    private static void exchangeWithEquipSlotOfGroup(InventoryImage clickedItem,
                                                     ItemSlot clickedSlot,
                                                     ItemSlot targetSlot) {
        clickedSlot.clearStack();
        new ItemSlotsExchanger(clickedItem, clickedSlot, targetSlot).exchange();
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    static void handleEquip(ItemSlot clickedSlot, DragAndDrop dragAndDrop) {
        clickedSlot.getPossibleInventoryImage()
                   .ifPresent(clickedItem -> exchangeWithPossibleEmptyInventorySlot(clickedItem, clickedSlot));
    }

    private static void exchangeWithPossibleEmptyInventorySlot(InventoryImage clickedItem, ItemSlot clickedSlot) {
        InventoryUtils.getScreenUI()
                      .getInventorySlotsTable()
                      .getPossibleEmptySlot()
                      .ifPresent(targetSlot -> exchangeWithEmptyInventorySlot(clickedItem, clickedSlot, targetSlot));
    }

    private static void exchangeWithEmptyInventorySlot(InventoryImage clickedItem,
                                                       ItemSlot clickedSlot,
                                                       ItemSlot targetSlot) {
        clickedSlot.clearStack();
        new ItemSlotsExchanger(clickedItem, clickedSlot, targetSlot).exchange();
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
        if (isHandlingFullStack(startAmount)) {
            dragImage = createDuplicate(dragImage, startAmount, dragAndDrop);
        } else if (isHandlingPartOfStack(startAmount)) {
            duplicateInSource(dragImage, sourceSlot, startAmount, dragAndDrop);
            int dragAmount = getDragAmount(sourceSlot);
            dragImage.setAmount(dragAmount);
            sourceSlot.decrementAmountBy(dragAmount);
        } else {
            throw new IllegalAccessError("Double click shop cannot reach this.");
        }
        new ItemSlotsExchanger(dragImage, sourceSlot, targetSlot).exchange();
    }

    private static boolean isHandlingFullStack(int startAmount) {
        return startAmount == 1 || InventoryUtils.isShiftPressed();
    }

    private static boolean isHandlingPartOfStack(int startAmount) {
        return startAmount > 1 && !InventoryUtils.isShiftPressed();
    }

    private static int getDragAmount(ItemSlot sourceSlot) {
        if (InventoryUtils.isCtrlPressed()) {
            return sourceSlot.getHalfOfAmount();
        } else {
            return 1;
        }
    }

    private static void duplicateInSource(InventoryImage dragImage,
                                          ItemSlot sourceSlot,
                                          int startAmount,
                                          DragAndDrop dragAndDrop) {
        var inventoryItem = new InventoryItem(dragImage.inventoryItem);
        inventoryItem.setAmount(startAmount);
        var inventoryImage = new InventoryImage(inventoryItem);
        sourceSlot.addToStack(inventoryImage);
        var itemSlotSource = new ItemSlotSource(inventoryImage, dragAndDrop);
        dragAndDrop.addSource(itemSlotSource);
    }

    private static InventoryImage createDuplicate(InventoryImage dragImage, int dragAmount, DragAndDrop dragAndDrop) {
        var inventoryItem = new InventoryItem(dragImage.inventoryItem);
        inventoryItem.setAmount(dragAmount);
        var inventoryImage = new InventoryImage(inventoryItem);
        var itemSlotSource = new ItemSlotSource(inventoryImage, dragAndDrop);
        dragAndDrop.addSource(itemSlotSource);
        return inventoryImage;
    }

}
