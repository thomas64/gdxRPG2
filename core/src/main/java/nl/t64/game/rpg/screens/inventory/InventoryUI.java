package nl.t64.game.rpg.screens.inventory;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.scenes.scene2d.utils.DragAndDrop;
import com.badlogic.gdx.scenes.scene2d.utils.NinePatchDrawable;
import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable;
import nl.t64.game.rpg.Utils;
import nl.t64.game.rpg.components.inventory.GlobalContainer;

import java.util.stream.IntStream;


class InventoryUI {

    private static final String FONT_TYPE = "fonts/fff_tusj.ttf";
    private static final int FONT_SIZE = 30;
    private static final String SPRITE_SILHOUETTE = "sprites/silhouette.png";
    private static final String SPRITE_BORDER = "sprites/border.png";

    private static final float SLOT_SIZE = 64f;
    private static final int SLOTS_IN_ROW = 5;
    private static final float TITLE_PADDING = 50f;

    private DragAndDrop dragAndDrop;
    private InventorySlotTooltip tooltip;

    Table equipSlotsTable;
    Window inventorySlotsWindow;
    Window playerSlotsWindow;

    InventoryUI() {
        this.dragAndDrop = new DragAndDrop();
        this.tooltip = new InventorySlotTooltip();
        createInventorySlotsTable();
        createPlayerSlotsTable();
    }

    private static Skin createWindowSkin() {
        var windowSkin = new Skin();
        var texture = Utils.getResourceManager().getTextureAsset(SPRITE_BORDER);
        var ninepatch = new NinePatch(texture, 1, 1, 1, 1);
        var drawable = new NinePatchDrawable(ninepatch);
        BitmapFont font = Utils.getResourceManager().getTrueTypeAsset(FONT_TYPE, FONT_SIZE);
        windowSkin.add("default", new Window.WindowStyle(font, Color.BLACK, drawable));
        return windowSkin;
    }

    private void createInventorySlotsTable() {
        inventorySlotsWindow = new Window("Global Inventory", createWindowSkin());
        GlobalContainer container = Utils.getGameData().getInventory();
        IntStream.rangeClosed(0, container.getLastIndex()).forEach(index -> {
            var inventorySlot = new InventorySlot();
            inventorySlot.addListener(new InventorySlotTooltipListener(tooltip));
            dragAndDrop.addTarget(new InventorySlotTarget(inventorySlot));
            dragAndDrop.addSource(new InventorySlotSource(inventorySlot.amountLabel, dragAndDrop));
            container.getItemAt(index).ifPresent(inventoryItem -> {
                inventorySlot.amount = container.getAmountOfItemAt(index);
                inventorySlot.addToStack(new InventoryImage(inventoryItem));
                dragAndDrop.addSource(new InventorySlotSource(inventorySlot.getCertainInventoryImage(), dragAndDrop));
            });
            inventorySlotsWindow.add(inventorySlot).size(SLOT_SIZE, SLOT_SIZE);
            if ((index + 1) % SLOTS_IN_ROW == 0) {
                inventorySlotsWindow.row();
            }
        });

//        inventorySlotsWindow.debugAll();
        inventorySlotsWindow.padTop(TITLE_PADDING);
        inventorySlotsWindow.pack();
    }

    private void createPlayerSlotsTable() {
        playerSlotsWindow = new Window("Personal Inventory", createWindowSkin());
        var texture = Utils.getResourceManager().getTextureAsset(SPRITE_SILHOUETTE);
        var sprite = new Sprite(texture);
        var silhouette = new SpriteDrawable(sprite);
        equipSlotsTable = new EquipSlotsTable(dragAndDrop, tooltip).equipSlots;
        equipSlotsTable.setBackground(silhouette);
        playerSlotsWindow.add(equipSlotsTable);

//        playerSlotsWindow.debugAll();
        playerSlotsWindow.padTop(TITLE_PADDING);
        playerSlotsWindow.pack();
    }

    void addToStage(Stage stage) {
        stage.addActor(inventorySlotsWindow);
        stage.addActor(playerSlotsWindow);
        stage.addActor(tooltip.window);
    }

}
