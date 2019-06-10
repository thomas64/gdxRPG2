package nl.t64.game.rpg.screens.inventory;

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


class InventoryUI {

    private static final String TITLE_FONT = "fonts/fff_tusj.ttf";
    private static final int TITLE_SIZE = 30;
    private static final String SPRITE_BORDER = "sprites/border.png";

    private static final String TITLE_GLOBAL = "Global Inventory";
    private static final String TITLE_PERSONAL = "Personal Inventory";
    private static final String TITLE_STATS = "Stats";
    private static final String TITLE_HEROES = "Heroes";
    private static final float TITLE_PADDING = 50f;

    final Window inventoryWindow;
    final Window equipWindow;
    final Window statsWindow;
    final Window heroesWindow;
    private final InventorySlotTooltip tooltip;
    private final StatsTable statsTable;
    private final HeroesTable heroesTable;

    InventoryUI() {
        final var dragAndDrop = new DragAndDrop();
        this.tooltip = new InventorySlotTooltip();

        final var inventorySlotsTable = new InventorySlotsTable(dragAndDrop, tooltip);
        this.inventoryWindow = createWindow(TITLE_GLOBAL, inventorySlotsTable.inventorySlots);
        final var equipSlotsTable = new EquipSlotsTable(dragAndDrop, tooltip);
        this.equipWindow = createWindow(TITLE_PERSONAL, equipSlotsTable.equipSlots);
        this.statsTable = new StatsTable();
        this.statsWindow = createWindow(TITLE_STATS, this.statsTable.stats);
        this.heroesTable = new HeroesTable();
        this.heroesWindow = createWindow(TITLE_HEROES, this.heroesTable.heroes);

        inventorySlotsTable.addObserver(this.statsTable);
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
        stage.addActor(heroesWindow);
        stage.addActor(tooltip.window);
    }

    void render() {
        statsTable.render();
        heroesTable.render();
        statsWindow.pack();
        heroesWindow.pack();
    }

    private Window createWindow(String title, Table table) {
        var window = new Window(title, createWindowStyle());
        window.add(table);
//        window.debugAll();
        window.padTop(TITLE_PADDING);
        window.getTitleLabel().setAlignment(Align.center);
        window.pack();
        return window;
    }

}
