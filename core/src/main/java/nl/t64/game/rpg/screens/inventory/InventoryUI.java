package nl.t64.game.rpg.screens.inventory;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.DragAndDrop;
import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable;
import nl.t64.game.rpg.Utils;
import nl.t64.game.rpg.components.inventory.GlobalContainer;

import java.util.stream.IntStream;


class InventoryUI {

    private static final String SPRITE_SILHOUETTE = "sprites/silhouette.png";
    private static final int SLOT_SIZE = 64;
    private static final int SLOTS_IN_ROW = (Gdx.graphics.getWidth() / SLOT_SIZE) - 2;
    private static final int PADDING = 50;

    private DragAndDrop dragAndDrop;
    private InventorySlotTooltip tooltip;

    private Table table;
    Table equipSlotsTable;
    Table inventorySlotsTable;
    Table playerSlotsTable;

    InventoryUI() {
        this.dragAndDrop = new DragAndDrop();
        this.tooltip = new InventorySlotTooltip();
        createInventorySlotsTable();
        createPlayerSlotsTable();
        createLayout();
    }

    private void createInventorySlotsTable() {
        inventorySlotsTable = new Table();
        GlobalContainer container = Utils.getGameData().getInventory();
        IntStream.rangeClosed(0, container.getSize() - 1).forEach(index -> {
            var inventorySlot = new InventorySlot();
            inventorySlot.addListener(new InventorySlotTooltipListener(tooltip));
            dragAndDrop.addTarget(new InventorySlotTarget(inventorySlot));
            dragAndDrop.addSource(new InventorySlotSource(inventorySlot.amountLabel, dragAndDrop));
            container.getItemAt(index).ifPresent(inventoryItem -> {
                inventorySlot.amount = container.getAmountOfItemAt(index);
                inventorySlot.addToStack(new InventoryImage(inventoryItem));
                dragAndDrop.addSource(new InventorySlotSource(inventorySlot.getCertainInventoryImage(), dragAndDrop));
            });
            inventorySlotsTable.add(inventorySlot).size(SLOT_SIZE, SLOT_SIZE);
            if ((index + 1) % SLOTS_IN_ROW == 0) {
                inventorySlotsTable.row();
            }
        });
    }

    private void createPlayerSlotsTable() {
        playerSlotsTable = new Table();
//        playerSlotsTable.debugAll();
        var texture = Utils.getResourceManager().getTextureAsset(SPRITE_SILHOUETTE);
        var sprite = new Sprite(texture);
        var silhouette = new SpriteDrawable(sprite);
        playerSlotsTable.setBackground(silhouette);
        equipSlotsTable = new EquipSlotsTable(dragAndDrop, tooltip).equipSlots;
        playerSlotsTable.add(equipSlotsTable);
    }

    private void createLayout() {
        table = new Table();
//        table.debugAll();
        table.add(playerSlotsTable).right().padBottom(PADDING).padRight(PADDING);
        table.row();
        table.add(inventorySlotsTable).row();
        table.pack();
    }

    void setPosition(float x, float y) {
        table.setPosition(x, y);
    }

    void addToStage(Stage stage) {
        stage.addActor(table);
        stage.addActor(tooltip.window);
    }

}
