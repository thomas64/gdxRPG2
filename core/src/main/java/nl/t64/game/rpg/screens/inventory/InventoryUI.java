package nl.t64.game.rpg.screens.inventory;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.scenes.scene2d.utils.DragAndDrop;
import com.badlogic.gdx.scenes.scene2d.utils.NinePatchDrawable;
import com.badlogic.gdx.utils.Align;
import nl.t64.game.rpg.Utils;


class InventoryUI {

    private static final String TITLE_FONT = "fonts/fff_tusj.ttf";
    private static final int TITLE_SIZE = 30;
    private static final String SPRITE_BORDER = "sprites/border.png";

    private static final String TITLE_GLOBAL = "Global Inventory";
    private static final String TITLE_PERSONAL = "Personal Inventory";
    private static final String TITLE_STATS = "Stats";
    private static final float TITLE_PADDING = 50f;

    private DragAndDrop dragAndDrop;
    private InventorySlotTooltip tooltip;
    Window equipWindow;
    private Window.WindowStyle windowStyle;

    Window inventoryWindow;
    private StatsTable statsTable;
    Window statsWindow;

    InventoryUI() {
        this.dragAndDrop = new DragAndDrop();
        this.tooltip = new InventorySlotTooltip();
        this.windowStyle = createWindowStyle();

        createInventoryWindow();
        createPlayerWindow();
        createStatsWindow();
    }

    private static Window.WindowStyle createWindowStyle() {
        var texture = Utils.getResourceManager().getTextureAsset(SPRITE_BORDER);
        var ninepatch = new NinePatch(texture, 1, 1, 1, 1);
        var drawable = new NinePatchDrawable(ninepatch);
        BitmapFont font = Utils.getResourceManager().getTrueTypeAsset(TITLE_FONT, TITLE_SIZE);
        return new Window.WindowStyle(font, Color.BLACK, drawable);
    }

    void addToStage(Stage stage) {
        stage.addActor(inventoryWindow);
        stage.addActor(equipWindow);
        stage.addActor(statsWindow);
        stage.addActor(tooltip.window);
    }

    void render() {
        statsTable.render();
        statsWindow.pack();
    }

    private void createInventoryWindow() {
        inventoryWindow = new Window(TITLE_GLOBAL, windowStyle);
        var inventorySlotsTable = new InventorySlotsTable(dragAndDrop, tooltip);
        inventoryWindow.add(inventorySlotsTable.inventorySlots);

//        inventoryWindow.debugAll();
        inventoryWindow.padTop(TITLE_PADDING);
        inventoryWindow.getTitleLabel().setAlignment(Align.center);
        inventoryWindow.pack();
    }

    private void createPlayerWindow() {
        equipWindow = new Window(TITLE_PERSONAL, windowStyle);
        var equipSlotsTable = new EquipSlotsTable(dragAndDrop, tooltip);
        equipWindow.add(equipSlotsTable.equipSlots);

//        equipWindow.debugAll();
        equipWindow.padTop(TITLE_PADDING);
        equipWindow.getTitleLabel().setAlignment(Align.center);
        equipWindow.pack();
    }

    private void createStatsWindow() {
        statsWindow = new Window(TITLE_STATS, windowStyle);
        statsTable = new StatsTable();
        statsWindow.add(statsTable.stats);

//        statsWindow.debugAll();
        statsWindow.padTop(TITLE_PADDING);
        statsWindow.getTitleLabel().setAlignment(Align.center);
        statsWindow.pack();
    }

}
