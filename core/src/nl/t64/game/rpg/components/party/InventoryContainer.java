package nl.t64.game.rpg.components.party;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;


public class InventoryContainer {

    private static final int NUMBER_OF_SLOTS = 66;

    private final List<InventoryItem> inventory;

    public InventoryContainer() {
        this(NUMBER_OF_SLOTS);
    }

    public InventoryContainer(int numberOfSlots) {
        this.inventory = new ArrayList<>(Collections.nCopies(numberOfSlots, null));
    }

    public List<InventoryItem> getAllFilledSlots() {
        return inventory.stream()
                        .filter(Objects::nonNull)
                        .collect(Collectors.toList());
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
                                         throwException("There is no item to decrement amount."));
    }

    public Optional<InventoryItem> getItemAt(int index) {
        return Optional.ofNullable(inventory.get(index));
    }

    public void autoSetItem(InventoryItem newItem) {
        if (newItem.isStackable()) {
            addResource(newItem);
        } else {
            addItemAtEmptySlot(newItem);
        }
    }

    public void autoRemoveItem(String itemId, int amount) {
        if (!hasEnoughOfItem(itemId, amount)) {
            throw new IllegalStateException("Cannot remove this resource from Inventory.");
        }
        for (Map.Entry<Integer, Integer> set : findAllSlotsWithAmountOfItem(itemId).entrySet()) {
            if (amount == 0) {
                break;
            } else if (amount >= set.getValue()) {
                amount -= set.getValue();
                clearItemAt(set.getKey());
            } else {
                decrementAmountAt(set.getKey(), amount);
                amount = 0;
            }
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

    public boolean isEmpty() {
        return inventory.stream().noneMatch(Objects::nonNull);
    }

    public void sort() {
        new InventoryStacksMerger(this).searchAll();
        Comparator<InventoryItem> comparing = Comparator.comparing(this::getInventoryGroup)
                                                        .thenComparing(this::getSort);
        inventory.sort(comparing);
    }

    public boolean contains(Map<String, Integer> items) {
        return items.entrySet()
                    .stream()
                    .allMatch(item -> hasEnoughOfItem(item.getKey(), item.getValue()));
    }

    public boolean hasEnoughOfItem(String itemId, int amount) {
        return getTotalOfItem(itemId) >= amount;
    }

    public boolean hasRoomForResource(String itemId) {
        return findFirstSlotWithItem(itemId).isPresent()
               || findFirstEmptySlot().isPresent();
    }

    int getLastIndex() {
        return getSize() - 1;
    }

    boolean contains(String itemId) {
        return inventory.stream()
                        .filter(Objects::nonNull)
                        .anyMatch(item -> item.hasSameIdAs(itemId));
    }

    int getTotalOfItem(String itemId) {
        return inventory.stream()
                        .filter(Objects::nonNull)
                        .filter(item -> item.hasSameIdAs(itemId))
                        .mapToInt(item -> item.amount)
                        .sum();
    }

    long getNumberOfFilledSlots() {
        return inventory.stream()
                        .filter(Objects::nonNull)
                        .count();
    }

    private void addResource(InventoryItem newItem) {
        getItem(newItem.id).ifPresentOrElse(inventoryItem -> inventoryItem.increaseAmountWith(newItem),
                                            () -> addItemAtEmptySlot(newItem)
        );
    }

    private void addItemAtEmptySlot(InventoryItem newItem) {
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

    private Map<Integer, Integer> findAllSlotsWithAmountOfItem(String itemId) {
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
        return IntStream.range(0, getSize()).boxed()
                        .filter(index -> containsItemAt(index, itemId))
                        .findFirst();
    }

    public OptionalInt findFirstFilledSlot() {
        return IntStream.range(0, getSize())
                        .filter(index -> !isSlotEmpty(index))
                        .findFirst();
    }

    public Optional<Integer> findFirstEmptySlot() {
        return IntStream.range(0, getSize()).boxed()
                        .filter(this::isSlotEmpty)
                        .findFirst();
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
