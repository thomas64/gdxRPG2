package nl.t64.game.rpg.components.party;

import java.util.*;


public class GlobalContainer {

    private static final int NUMBER_OF_SLOTS = 77;

    private final List<InventoryItem> inventory;

    public GlobalContainer() {
        this.inventory = new ArrayList<>(Collections.nCopies(NUMBER_OF_SLOTS, null));
    }

    public int getAmountOfItemAt(int index) {
        return getItemAt(index).map(item -> item.amount)
                               .orElse(0);
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
        return getSize() - 1;
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

    private int getSize() {
        return inventory.size();
    }

    private void addResource(InventoryItem newItem) {
        getItem(newItem.id).ifPresentOrElse(
                inventoryItem -> inventoryItem.increaseAmountWith(newItem), // todo, aanpassen, mag niet oneindig ophogen
                addResourceAtEmptySlot(newItem)
        );
    }

    private Runnable addResourceAtEmptySlot(InventoryItem newItem) {
        return () -> findLastEmptySlot().ifPresentOrElse(
                index -> slotIsEmptySoSetItemAt(index, newItem),
                () -> {
                    throw new IllegalStateException("Inventory is full.");
                });
    }

    private void addEquipmentAtEmptySlot(InventoryItem newItem) {
        findFirstEmptySlot().ifPresentOrElse(
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
