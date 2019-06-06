package nl.t64.game.rpg.components.inventory;

import java.util.*;


public class GlobalContainer {

    private static final int NUMBER_OF_SLOTS = 70;

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

    public void autoSetItem(InventoryItem newItem) {
        if (newItem.group.equals(InventoryGroup.RESOURCE)) {
            addResource(newItem);
        } else {
            addEquipmentAtEmptySlot(newItem);
        }
    }

    public void forceSetItemAt(int index, InventoryItem newItem) {
        inventory.set(index, newItem);
    }

    public int getLastIndex() {
        return inventory.size() - 1;
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

    private void addResource(InventoryItem newItem) {
        getItem(newItem.id).ifPresentOrElse(
                inventoryItem -> inventoryItem.increaseAmountWith(newItem),
                addResourceAtEmptySlot(newItem)
        );
    }

    private Runnable addResourceAtEmptySlot(InventoryItem newItem) {
        return () -> findFirstEmptySlot().ifPresentOrElse(
                index -> slotIsEmptySoSetItemAt(index, newItem),
                () -> {
                    throw new IllegalStateException("Inventory is full.");
                });
    }

    private void addEquipmentAtEmptySlot(InventoryItem newItem) {
        findLastEmptySlot().ifPresentOrElse(
                index -> slotIsEmptySoSetItemAt(index, newItem),
                () -> {
                    throw new IllegalStateException("Inventory is full.");
                });
    }

    private void slotIsEmptySoSetItemAt(int index, InventoryItem newItem) {
        forceSetItemAt(index, newItem);
    }

    private Optional<InventoryItem> getItem(String id) {
        return inventory.stream()
                        .filter(Objects::nonNull)
                        .filter(item -> item.hasSameIdAs(id))
                        .findFirst();
    }

    private OptionalInt findFirstEmptySlot() {
        for (int i = 0; i <= getLastIndex(); i++) {
            if (isSlotEmpty(i)) {
                return OptionalInt.of(i);
            }
        }
        return OptionalInt.empty();
    }

    private OptionalInt findLastEmptySlot() {
        for (int i = getLastIndex(); i > -1; i--) {
            if (isSlotEmpty(i)) {
                return OptionalInt.of(i);
            }
        }
        return OptionalInt.empty();
    }

    private boolean isSlotEmpty(int index) {
        return inventory.get(index) == null;
    }

}