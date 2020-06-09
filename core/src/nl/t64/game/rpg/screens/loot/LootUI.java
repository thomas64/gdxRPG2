package nl.t64.game.rpg.screens.loot;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.scenes.scene2d.utils.DragAndDrop;
import com.badlogic.gdx.scenes.scene2d.utils.NinePatchDrawable;
import com.badlogic.gdx.utils.Align;
import nl.t64.game.rpg.Utils;
import nl.t64.game.rpg.components.loot.Loot;
import nl.t64.game.rpg.screens.inventory.EquipSlotsTable;
import nl.t64.game.rpg.screens.inventory.InventorySlotsTable;
import nl.t64.game.rpg.screens.inventory.ListenerMouseScrollPane;
import nl.t64.game.rpg.screens.inventory.ScreenUI;
import nl.t64.game.rpg.screens.inventory.tooltip.ItemSlotTooltip;
import nl.t64.game.rpg.screens.inventory.tooltip.LootSlotTooltip;

import java.util.Collections;
import java.util.Map;


class LootUI implements ScreenUI {

    private static final String TITLE_FONT = "fonts/fff_tusj.ttf";
    private static final int TITLE_SIZE = 30;
    private static final String SPRITE_BORDER = "sprites/border.png";

    private static final String TITLE_GLOBAL = "   Inventory";
    private static final float INVENTORY_HEIGHT = 224f;
    private static final float TITLE_PADDING = 50f;

    final Window inventoryWindow;
    final Window lootWindow;
    private final LootSlotsTable lootSlotsTable;
    private final InventorySlotsTable inventorySlotsTable;
    private final DragAndDrop dragAndDrop;
    private final ItemSlotTooltip lootSlotTooltip;

    LootUI(Loot loot, String title) {
        this.dragAndDrop = new DragAndDrop();
        this.lootSlotTooltip = new LootSlotTooltip();

        this.inventorySlotsTable = new InventorySlotsTable(this.dragAndDrop, this.lootSlotTooltip, INVENTORY_HEIGHT);
        this.inventoryWindow = createWindow(TITLE_GLOBAL, this.inventorySlotsTable.container);

        this.lootSlotsTable = new LootSlotsTable(loot, this.dragAndDrop, this.lootSlotTooltip);
        this.lootWindow = createWindow(title, this.lootSlotsTable.container);
    }

    private static Window.WindowStyle createWindowStyle() {
        var texture = Utils.getResourceManager().getTextureAsset(SPRITE_BORDER);
        var ninepatch = new NinePatch(texture, 1, 1, 1, 1);
        var drawable = new NinePatchDrawable(ninepatch);
        BitmapFont font = Utils.getResourceManager().getTrueTypeAsset(TITLE_FONT, TITLE_SIZE);
        return new Window.WindowStyle(font, Color.BLACK, drawable);
    }

    @Override
    public Window getEquipWindow() {
        throw new IllegalAccessError("Double click loot cannot reach this.");
    }

    @Override
    public Map<String, EquipSlotsTable> getEquipSlotsTables() {
        return Collections.emptyMap();
    }

    @Override
    public InventorySlotsTable getInventorySlotsTable() {
        return inventorySlotsTable;
    }

    boolean takeItem() {
        return lootSlotsTable.takeItem();
    }

    boolean isEmpty() {
        return lootSlotsTable.isEmpty();
    }

    Map<String, Integer> getContent() {
        return lootSlotsTable.getContent();
    }

    void addToStage(Stage stage) {
        stage.addActor(inventoryWindow);
        stage.addActor(lootWindow);
        lootSlotTooltip.addToStage(stage);
    }

    void applyListeners(Stage stage) {
        lootWindow.addListener(new ListenerMouseScrollPane(stage, lootSlotsTable.scrollPane));
        inventoryWindow.addListener(new ListenerMouseScrollPane(stage, inventorySlotsTable.scrollPane));
    }

    void update() {
        // empty
    }

    void unloadAssets() {
        // empty
    }

    private Window createWindow(String title, Table table) {
        var window = new Window(title, createWindowStyle());
        window.add(table);
        window.padTop(TITLE_PADDING);
        window.setMovable(false);
        window.getTitleLabel().setAlignment(Align.left);
        window.pack();
        return window;
    }

}
