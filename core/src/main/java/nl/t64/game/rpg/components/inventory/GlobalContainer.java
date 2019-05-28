package nl.t64.game.rpg.components.inventory;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class GlobalContainer {

    private final List<InventoryItem> inventory;

    public GlobalContainer() {
        this.inventory = new ArrayList<>(Collections.nCopies(84, null));
    }

    int getAmountOfItemAt(int index) {
        InventoryItem targetItem = inventory.get(index);
        if (targetItem == null) {
            return 0;
        } else {
            return targetItem.amount;
        }
    }

    public void setItemAt(int index, InventoryItem sourceItem) {
        InventoryItem targetItem = inventory.get(index);
        if (targetItem == null) {
            inventory.set(index, sourceItem);
        } else {
            setItemAtFilledSpot(sourceItem, targetItem);
        }
    }

    private void setItemAtFilledSpot(InventoryItem sourceItem, InventoryItem targetItem) {
        if (sourceItem.group.equals(InventoryGroup.RESOURCE)) {
            if (sourceItem.id.equals(targetItem.id)) {
                targetItem.amount += sourceItem.amount;
            } else {
                throw new UnsupportedOperationException();
            }
        } else {
            throw new UnsupportedOperationException();
        }
    }

    boolean contains(String id) {
        return inventory.stream()
                        .filter(Objects::nonNull)
                        .anyMatch(item -> item.id.equals(id));
    }

    long getSize() {
        return inventory.stream()
                        .filter(Objects::nonNull)
                        .count();
    }

}
