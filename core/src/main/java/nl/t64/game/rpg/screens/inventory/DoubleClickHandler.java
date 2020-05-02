package nl.t64.game.rpg.screens.inventory;

import com.badlogic.gdx.scenes.scene2d.utils.DragAndDrop;
import nl.t64.game.rpg.components.party.InventoryItem;


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
        InventoryUtils.getScreenUI()
                      .getEquipSlotsTables()
                      .get(InventoryUtils.getSelectedHero().getId())
                      .getPossibleSlotOfGroup(clickedItem.inventoryGroup)
                      .ifPresent(targetSlot -> exchangeWithEquipSlotOfGroup(clickedItem, clickedSlot, targetSlot));
    }

    private static void exchangeWithEquipSlotOfGroup(InventoryImage clickedItem,
                                                     ItemSlot clickedSlot,
                                                     ItemSlot targetSlot) {
        clickedSlot.decrementAmount();
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
        clickedSlot.decrementAmount();
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

    private static void exchangeWithInventorySlot(InventoryImage clickedItem,
                                                  ItemSlot clickedSlot,
                                                  ItemSlot targetSlot,
                                                  DragAndDrop dragAndDrop) {
        int newAmount = clickedSlot.getAmount() - 1;
        clickedSlot.clearStack();
        if (newAmount > 0) {
            clickedItem.inventoryItem.setAmount(1);
            duplicateInSource(clickedItem, clickedSlot, newAmount, dragAndDrop);
        } else {
            clickedItem = createDuplicate(clickedItem, dragAndDrop);
        }
        new ItemSlotsExchanger(clickedItem, clickedSlot, targetSlot).exchange();
    }

    private static void duplicateInSource(InventoryImage clickedItem,
                                          ItemSlot clickedSlot,
                                          int newAmount,
                                          DragAndDrop dragAndDrop) {
        var inventoryItem = new InventoryItem(clickedItem.inventoryItem);
        inventoryItem.setAmount(newAmount);
        var inventoryImage = new InventoryImage(inventoryItem);
        clickedSlot.addToStack(inventoryImage);
        var itemSlotSource = new ItemSlotSource(inventoryImage, dragAndDrop);
        dragAndDrop.addSource(itemSlotSource);
    }

    private static InventoryImage createDuplicate(InventoryImage clickedItem, DragAndDrop dragAndDrop) {
        var inventoryItem = new InventoryItem(clickedItem.inventoryItem);
        var inventoryImage = new InventoryImage(inventoryItem);
        var itemSlotSource = new ItemSlotSource(inventoryImage, dragAndDrop);
        dragAndDrop.addSource(itemSlotSource);
        return inventoryImage;
    }

}
