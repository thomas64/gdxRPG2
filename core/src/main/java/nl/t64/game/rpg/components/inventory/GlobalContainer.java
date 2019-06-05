package nl.t64.game.rpg.components.inventory;

import java.util.*;


public class GlobalContainer {

    private static final int NUMBER_OF_SLOTS = 84;

    private final List<InventoryItem> inventory;

    public GlobalContainer() {
        this.inventory = new ArrayList<>(Collections.nCopies(NUMBER_OF_SLOTS, null));
    }

    public int getAmountOfItemAt(int index) {
        InventoryItem targetItem = inventory.get(index);
        if (targetItem == null) {
            return 0;
        } else {
            return targetItem.amount;
        }
    }

    public Optional<InventoryItem> getItemAt(int index) {
        return Optional.ofNullable(inventory.get(index));
    }

    public void forceSetItemAt(int index, InventoryItem newItem) {
        inventory.set(index, newItem);
    }

    public int getSize() {
        return inventory.size();
    }

    void setItemAt(int index, InventoryItem sourceItem) {
        InventoryItem targetItem = inventory.get(index);
        if (targetItem == null) {
            inventory.set(index, sourceItem);
        } else {
            targetItem.receiveInventoryItem(sourceItem);
        }
    }

    boolean contains(String id) {
        return inventory.stream()
                        .filter(Objects::nonNull)
                        .anyMatch(item -> item.hasSameIdAs(id));
    }

    long getNumberOfFilledSlots() {
        return inventory.stream()
                        .filter(Objects::nonNull)
                        .count();
    }

}
