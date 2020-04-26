package nl.t64.game.rpg.screens.inventory;

import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.DragAndDrop;
import com.badlogic.gdx.scenes.scene2d.utils.NinePatchDrawable;
import nl.t64.game.rpg.Utils;
import nl.t64.game.rpg.components.party.InventoryContainer;
import nl.t64.game.rpg.components.party.InventoryGroup;
import nl.t64.game.rpg.components.party.InventoryItem;
import nl.t64.game.rpg.components.tooltip.ItemSlotTooltip;
import nl.t64.game.rpg.components.tooltip.ItemSlotTooltipListener;

import java.util.Optional;
import java.util.function.Consumer;
import java.util.stream.IntStream;


public class InventorySlotsTable {

    private static final String SPRITE_TOP_BORDER = "sprites/top_border.png";
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
        this.inventory = Utils.getGameData().getInventory();
        this.dragAndDrop = dragAndDrop;
        this.tooltip = tooltip;
        this.inventorySlotTable = new Table();
        fillInventorySlots();

        this.scrollPane = new ScrollPane(this.inventorySlotTable);
        this.container = new Table();
        this.container.add(this.scrollPane).height(CONTAINER_HEIGHT);
        setTopBorder();
    }

    void clearAndFill() {
        inventorySlotTable.clear();
        fillInventorySlots();
    }

    Optional<ItemSlot> getPossibleSameStackableItemSlotWith(InventoryItem candidateItem) {
        if (candidateItem.isStackable()) {
            return inventory.findFirstSlotWithItem(candidateItem.getId())
                            .map(i -> (ItemSlot) inventorySlotTable.getChildren().get(i));
        } else {
            return Optional.empty();
        }
    }

    Optional<ItemSlot> getPossibleEmptySlot() {
        return inventory.findFirstEmptySlot()
                        .map(i -> (ItemSlot) inventorySlotTable.getChildren().get(i));
    }

    private void fillInventorySlots() {
        IntStream.range(0, inventory.getSize())
                 .forEach(this::createInventorySlot);
    }

    private void setTopBorder() {
        var texture = Utils.getResourceManager().getTextureAsset(SPRITE_TOP_BORDER);
        var ninepatch = new NinePatch(texture, 0, 0, 1, 0);
        var drawable = new NinePatchDrawable(ninepatch);
        container.setBackground(drawable);
    }

    private void createInventorySlot(int index) {
        InventorySlot inventorySlot = new InventorySlot(index, InventoryGroup.EVERYTHING, inventory);
        inventorySlot.addListener(new ItemSlotTooltipListener(tooltip));
        inventorySlot.addListener(new ItemSlotClickListener(DoubleClickHandler::handleInventory));
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
            inventorySlot.addToStack(inventoryImage);
            var itemSlotSource = new ItemSlotSource(inventoryImage, dragAndDrop);
            dragAndDrop.addSource(itemSlotSource);
        };
    }

}
