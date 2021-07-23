package nl.t64.game.rpg.components.party;

import java.util.stream.IntStream;


class InventoryStacksMerger {

    private final InventoryContainer container;
    private InventoryItem inventoryItem1;
    private InventoryItem inventoryItem2;

    InventoryStacksMerger(InventoryContainer container) {
        this.container = container;
        this.inventoryItem1 = null;
        this.inventoryItem2 = null;
    }

    void searchAll() {
        IntStream.range(0, container.getSize())
                 .forEach(this::setItem1IfPresent);
    }

    private void setItem1IfPresent(int index1) {
        InventoryItem item = container.getItemAt(index1);
        if (item != null) {
            inventoryItem1 = item;
            searchIfStackable(index1);
        }
    }

    private void searchIfStackable(int index1) {
        if (inventoryItem1.isStackable()) {
            searchLeftoverForSameItem(index1 + 1);
        }
    }

    private void searchLeftoverForSameItem(int index2) {
        IntStream.range(index2, container.getSize())
                 .forEach(this::setItem2IfPresent);
    }

    private void setItem2IfPresent(int index2) {
        InventoryItem item = container.getItemAt(index2);
        if (item != null) {
            inventoryItem2 = item;
            stackIfItemsAreTheSame(index2);
        }
    }

    private void stackIfItemsAreTheSame(int index2) {
        if (inventoryItem1.hasSameIdAs(inventoryItem2)) {
            stackTwoItems(index2);
        }
    }

    private void stackTwoItems(int index2) {
        inventoryItem1.increaseAmountWith(inventoryItem2);
        container.clearItemAt(index2);
    }

}
