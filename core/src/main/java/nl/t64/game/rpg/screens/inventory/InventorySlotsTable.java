package nl.t64.game.rpg.screens.inventory;

import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.DragAndDrop;
import com.badlogic.gdx.scenes.scene2d.utils.NinePatchDrawable;
import nl.t64.game.rpg.Utils;
import nl.t64.game.rpg.components.party.GlobalContainer;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;


class InventorySlotsTable {

    private static final String SPRITE_TOP_BORDER = "sprites/top_border.png";
    private static final float SLOT_SIZE = 64f;
    private static final int SLOTS_IN_ROW = 6;

    final Table inventorySlots;
    private final List<SlotOverObserver> observers;
    private final DragAndDrop dragAndDrop;
    private final InventorySlotTooltip tooltip;

    InventorySlotsTable(DragAndDrop dragAndDrop, InventorySlotTooltip tooltip) {
        this.observers = new ArrayList<>();
        this.dragAndDrop = dragAndDrop;
        this.tooltip = tooltip;
        this.inventorySlots = new Table();

        GlobalContainer container = Utils.getGameData().getInventory();
        IntStream.rangeClosed(0, container.getLastIndex())
                 .forEach(index -> createInventorySlot(container, index));

        var texture = Utils.getResourceManager().getTextureAsset(SPRITE_TOP_BORDER);
        var ninepatch = new NinePatch(texture, 0, 0, 1, 0);
        var drawable = new NinePatchDrawable(ninepatch);
        this.inventorySlots.setBackground(drawable);
    }

    void addObserver(SlotOverObserver observer) {
        observers.add(observer);
    }

    private void createInventorySlot(GlobalContainer container, int index) {
        var inventorySlot = new InventorySlot();
        inventorySlot.addListener(new InventorySlotTooltipListener(tooltip));
        inventorySlot.addListener(new InventorySlotPreviewListener(this::sendInventoryItem));
        dragAndDrop.addTarget(new InventorySlotTarget(inventorySlot));
        dragAndDrop.addSource(new InventorySlotSource(inventorySlot.amountLabel, dragAndDrop));
        container.getItemAt(index).ifPresent(inventoryItem -> {
            inventorySlot.amount = container.getAmountOfItemAt(index);
            inventorySlot.addToStack(new InventoryImage(inventoryItem));
            dragAndDrop.addSource(new InventorySlotSource(inventorySlot.getCertainInventoryImage(), dragAndDrop));
        });
        inventorySlots.add(inventorySlot).size(SLOT_SIZE, SLOT_SIZE);
        if ((index + 1) % SLOTS_IN_ROW == 0) {
            inventorySlots.row();
        }
    }

    private void sendInventoryItem(InventorySlot inventorySlot) {
        inventorySlot.getPossibleInventoryImage().ifPresentOrElse(
                inventoryImage -> observers.forEach(observer -> observer.receive(inventoryImage.inventoryItem)),
                () -> observers.forEach(observer -> observer.receive(null)));
    }

}
