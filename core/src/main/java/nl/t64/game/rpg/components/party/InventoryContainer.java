package nl.t64.game.rpg.components.party;

import java.util.*;
import java.util.stream.IntStream;


public class InventoryContainer {

    private static final int NUMBER_OF_SLOTS = 66;

    private final List<InventoryItem> inventory;

    public InventoryContainer() {
        this.inventory = new ArrayList<>(Collections.nCopies(NUMBER_OF_SLOTS, null));
    }

    public int getAmountOfItemAt(int index) {
        return getItemAt(index).map(item -> item.amount)
                               .orElse(0);
    }

    public void incrementAmountAt(int index, int amount) {
        getItemAt(index).ifPresentOrElse(inventoryItem -> inventoryItem.increaseAmountWith(amount),
                                         throwException("There is no item to increment amount."));
    }

    public void decrementAmountAt(int index, int amount) {
        getItemAt(index).ifPresentOrElse(inventoryItem -> inventoryItem.decreaseAmountWith(amount),
                                         throwException("There is no item to increment amount."));
    }

    public Optional<InventoryItem> getItemAt(int index) {
        return Optional.ofNullable(inventory.get(index));
    }

    public void autoSetItem(InventoryItem newItem) {
        if (newItem.isStackable()) {
            addResource(newItem);
        } else {
            addEquipmentAtEmptySlot(newItem);
        }
    }

    public void clearItemAt(int index) {
        forceSetItemAt(index, null);
    }

    public void forceSetItemAt(int index, InventoryItem newItem) {
        inventory.set(index, newItem);
    }

    public int getSize() {
        return inventory.size();
    }

    public void sort() {
        new InventoryStacksMerger(this).searchAll();
        Comparator<InventoryItem> comparing = Comparator.comparing(this::getInventoryGroup)
                                                        .thenComparing(this::getSort);
        inventory.sort(comparing);
    }

    int getLastIndex() {
        return getSize() - 1;
    }

    boolean contains(String itemId) {
        return inventory.stream()
                        .filter(Objects::nonNull)
                        .anyMatch(item -> item.hasSameIdAs(itemId));
    }

    long getNumberOfFilledSlots() {
        return inventory.stream()
                        .filter(Objects::nonNull)
                        .count();
    }

    private void addResource(InventoryItem newItem) {
        getItem(newItem.id).ifPresentOrElse(inventoryItem -> inventoryItem.increaseAmountWith(newItem), // todo, aanpassen, mag niet oneindig ophogen
                                            addResourceAtEmptySlot(newItem)
        );
    }

    public boolean hasEnoughOfResource(String itemId, int amount) {
        return getTotalOfResource(itemId) >= amount;
    }

    private int getTotalOfResource(String itemId) {
        return inventory.stream()
                        .filter(Objects::nonNull)
                        .filter(item -> item.hasSameIdAs(itemId))
                        .mapToInt(item -> item.amount)
                        .sum();
    }

    private Runnable addResourceAtEmptySlot(InventoryItem newItem) {
        return () -> findLastEmptySlot().ifPresentOrElse(index -> slotIsEmptySoSetItemAt(index, newItem),
                                                         throwException("Inventory is full."));
    }

    private void addEquipmentAtEmptySlot(InventoryItem newItem) {
        findFirstEmptySlot().ifPresentOrElse(index -> slotIsEmptySoSetItemAt(index, newItem),
                                             throwException("Inventory is full."));
    }

    private void slotIsEmptySoSetItemAt(int index, InventoryItem newItem) {
        forceSetItemAt(index, newItem);
    }

    private Optional<InventoryItem> getItem(String itemId) {
        return inventory.stream()
                        .filter(Objects::nonNull)
                        .filter(item -> item.hasSameIdAs(itemId))
                        .findFirst();
    }

    public Map<Integer, Integer> findAllSlotsWithAmountOfResource(String itemId) {
        Map<Integer, Integer> resourceMap = new HashMap<>();
        IntStream.range(0, getSize())
                 .forEach(i -> {
                     InventoryItem item = inventory.get(i);
                     if (item != null && item.id.equals(itemId)) {
                         resourceMap.put(i, item.amount);
                     }
                 });
        return resourceMap;
    }

    public Optional<Integer> findFirstSlotWithItem(String itemId) {
        for (int i = 0; i < getSize(); i++) {
            if (containsItemAt(i, itemId)) {
                return Optional.of(i);
            }
        }
        return Optional.empty();
    }

    public Optional<Integer> findFirstEmptySlot() {
        for (int i = 0; i < getSize(); i++) {
            if (isSlotEmpty(i)) {
                return Optional.of(i);
            }
        }
        return Optional.empty();
    }

    private Optional<Integer> findLastEmptySlot() {
        for (int i = getLastIndex(); i >= 0; i--) {
            if (isSlotEmpty(i)) {
                return Optional.of(i);
            }
        }
        return Optional.empty();
    }

    private boolean isSlotEmpty(int index) {
        return inventory.get(index) == null;
    }

    private boolean containsItemAt(int index, String itemId) {
        return getItemAt(index).map(inventoryItem -> inventoryItem.hasSameIdAs(itemId))
                               .orElse(false);
    }

    private InventoryGroup getInventoryGroup(InventoryItem inventoryItem) {
        if (inventoryItem == null) {
            return InventoryGroup.EMPTY;
        }
        return inventoryItem.group;
    }

    private int getSort(InventoryItem inventoryItem) {
        if (inventoryItem == null) {
            return 0;
        }
        return inventoryItem.sort;
    }

    private Runnable throwException(String exceptionMessage) {
        return () -> {
            throw new IllegalStateException(exceptionMessage);
        };
    }

}
