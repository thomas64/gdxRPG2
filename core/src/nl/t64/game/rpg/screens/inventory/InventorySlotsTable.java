package nl.t64.game.rpg.screens.inventory;

import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.DragAndDrop;
import nl.t64.game.rpg.Utils;
import nl.t64.game.rpg.components.party.InventoryContainer;
import nl.t64.game.rpg.components.party.InventoryGroup;
import nl.t64.game.rpg.components.party.InventoryItem;
import nl.t64.game.rpg.screens.inventory.tooltip.ItemSlotTooltip;
import nl.t64.game.rpg.screens.inventory.tooltip.ItemSlotTooltipListener;

import java.util.Optional;
import java.util.function.Consumer;
import java.util.stream.IntStream;


public class InventorySlotsTable {

    private static final float SLOT_SIZE = 64f;
    private static final int SLOTS_IN_ROW = 6;
    private static final float CONTAINER_HEIGHT = 704f;

    public final Table container;
    public final ScrollPane scrollPane;
    private final Table inventorySlotTable;
    private final DragAndDrop dragAndDrop;
    private final ItemSlotTooltip tooltip;
    private final InventoryContainer inventory;

    public InventorySlotsTable(DragAndDrop dragAndDrop, ItemSlotTooltip tooltip) {
        this(dragAndDrop, tooltip, CONTAINER_HEIGHT);
    }

    public InventorySlotsTable(DragAndDrop dragAndDrop, ItemSlotTooltip tooltip, float containerHeight) {
        this.inventory = Utils.getGameData().getInventory();
        this.dragAndDrop = dragAndDrop;
        this.tooltip = tooltip;
        this.inventorySlotTable = new Table();
        fillInventorySlots();

        this.scrollPane = new ScrollPane(this.inventorySlotTable);
        this.container = new Table();
        this.container.add(this.scrollPane).height(containerHeight);
        container.setBackground(Utils.createTopBorder());
    }

    void addResource(InventoryItem inventoryItem) {
        inventory.autoSetItem(inventoryItem);
        clearAndFill();
    }

    void removeResource(String itemId, int amount) {
        inventory.autoRemoveItem(itemId, amount);
        clearAndFill();
    }

    void clearAndFill() {
        inventorySlotTable.clear();
        fillInventorySlots();
    }

    Optional<ItemSlot> getPossibleSameStackableItemSlotWith(InventoryItem candidateItem) {
        if (candidateItem.isStackable()) {
            return inventory.findFirstSlotWithItem(candidateItem.getId())
                            .map(index -> (ItemSlot) inventorySlotTable.getChildren().get(index));
        } else {
            return Optional.empty();
        }
    }

    Optional<ItemSlot> getPossibleEmptySlot() {
        return inventory.findFirstEmptySlot()
                        .map(index -> (ItemSlot) inventorySlotTable.getChildren().get(index));
    }

    private void fillInventorySlots() {
        IntStream.range(0, inventory.getSize())
                 .forEach(this::createInventorySlot);
    }

    private void createInventorySlot(int index) {
        InventorySlot inventorySlot = new InventorySlot(index, InventoryGroup.EVERYTHING, inventory);
        inventorySlot.addListener(new ItemSlotTooltipListener(tooltip));
        inventorySlot.addListener(new ItemSlotClickListener(DoubleClickHandler::handleInventory, dragAndDrop));
        dragAndDrop.addTarget(new ItemSlotTarget(inventorySlot));
        dragAndDrop.addSource(new ItemSlotSource(inventorySlot.amountLabel, dragAndDrop));
        inventory.getItemAt(index).ifPresent(addToSlot(inventorySlot));
        inventorySlotTable.add(inventorySlot).size(SLOT_SIZE, SLOT_SIZE);
        if ((index + 1) % SLOTS_IN_ROW == 0) {
            inventorySlotTable.row();
        }
    }

    private Consumer<InventoryItem> addToSlot(InventorySlot inventorySlot) {
        return inventoryItem -> {
            var inventoryImage = new InventoryImage(inventoryItem);
            makeDraggable(inventoryImage);
            inventorySlot.addToStack(inventoryImage);
        };
    }

    private void makeDraggable(InventoryImage inventoryImage) {
        var itemSlotSource = new ItemSlotSource(inventoryImage, dragAndDrop);
        dragAndDrop.addSource(itemSlotSource);
    }

}
