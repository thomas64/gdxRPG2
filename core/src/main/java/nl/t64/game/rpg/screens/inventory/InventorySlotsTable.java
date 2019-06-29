package nl.t64.game.rpg.screens.inventory;

import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.DragAndDrop;
import com.badlogic.gdx.scenes.scene2d.utils.NinePatchDrawable;
import nl.t64.game.rpg.Utils;
import nl.t64.game.rpg.components.party.InventoryContainer;

import java.util.Optional;
import java.util.stream.IntStream;


class InventorySlotsTable {

    private static final String SPRITE_TOP_BORDER = "sprites/top_border.png";
    private static final float SLOT_SIZE = 64f;
    private static final int SLOTS_IN_ROW = 7;

    final Table inventorySlots;
    private final DragAndDrop dragAndDrop;
    private final InventorySlotTooltip tooltip;
    private final InventoryContainer inventory;

    InventorySlotsTable(DragAndDrop dragAndDrop, InventorySlotTooltip tooltip) {
        this.inventory = Utils.getGameData().getInventory();
        this.dragAndDrop = dragAndDrop;
        this.tooltip = tooltip;
        this.inventorySlots = new Table();
        fillInventorySlots();
        setTopBorder();
    }

    Optional<InventorySlot> getPossibleEmptySlot() {
        return inventory.findFirstEmptySlot()
                        .map(i -> (InventorySlot) inventorySlots.getChildren().get(i));
    }

    private void fillInventorySlots() {
        IntStream.range(0, inventory.getSize())
                 .forEach(this::createInventorySlot);
    }

    private void setTopBorder() {
        var texture = Utils.getResourceManager().getTextureAsset(SPRITE_TOP_BORDER);
        var ninepatch = new NinePatch(texture, 0, 0, 1, 0);
        var drawable = new NinePatchDrawable(ninepatch);
        inventorySlots.setBackground(drawable);
    }

    private void createInventorySlot(int index) {
        var inventorySlot = new InventorySlot();
        inventorySlot.addListener(new InventorySlotTooltipListener(tooltip));
        inventorySlot.addListener(new InventorySlotPreviewListener(InventoryUtils::updateHoveredItem));
        inventorySlot.addListener(new InventorySlotClickListener(InventoryUtils::handleDoubleClickInventory));
        dragAndDrop.addTarget(new InventorySlotTarget(inventorySlot));
        dragAndDrop.addSource(new InventorySlotSource(inventorySlot.amountLabel, dragAndDrop));
        inventory.getItemAt(index).ifPresent(inventoryItem -> {
            inventorySlot.amount = inventory.getAmountOfItemAt(index);
            inventorySlot.addToStack(new InventoryImage(inventoryItem));
            dragAndDrop.addSource(new InventorySlotSource(inventorySlot.getCertainInventoryImage(), dragAndDrop));
        });
        inventorySlots.add(inventorySlot).size(SLOT_SIZE, SLOT_SIZE);
        if ((index + 1) % SLOTS_IN_ROW == 0) {
            inventorySlots.row();
        }
    }

}
