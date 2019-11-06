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
import nl.t64.game.rpg.components.party.HeroItem;
import nl.t64.game.rpg.components.party.PartyContainer;

import java.util.HashMap;
import java.util.Map;


class InventoryUI {

    private static final String TITLE_FONT = "fonts/fff_tusj.ttf";
    private static final int TITLE_SIZE = 30;
    private static final String SPRITE_BORDER = "sprites/border.png";

    private static final String TITLE_GLOBAL = "   Inventory";
    private static final String TITLE_PERSONAL = "   Equipment";
    private static final String TITLE_SPELLS = "   Spells";
    private static final String TITLE_SKILLS = "   Skills";
    private static final String TITLE_STATS = "   Stats";
    private static final String TITLE_CALCS = "   Calcs";
    private static final String TITLE_HEROES = "   Heroes";
    private static final float TITLE_PADDING = 50f;

    final Window spellsWindow;
    final Window inventoryWindow;
    final Window equipWindow;
    final Window skillsWindow;
    final Window statsWindow;
    final Window calcsWindow;
    final Window heroesWindow;

    private final SpellsTable spellsTable;
    final InventorySlotsTable inventorySlotsTable;
    final Map<String, EquipSlotsTable> equipSlotsTables;
    private final SkillsTable skillsTable;
    private final StatsTable statsTable;
    private final CalcsTable calcsTable;
    private final HeroesTable heroesTable;

    private final InventorySlotTooltip inventorySlotTooltip;
    private final StatTooltip personalityTooltip;

    InventoryUI() {
        final var dragAndDrop = new DragAndDrop();
        this.inventorySlotTooltip = new InventorySlotTooltip();
        this.personalityTooltip = new StatTooltip();

        this.spellsTable = new SpellsTable(this.personalityTooltip);
        this.spellsWindow = createWindow(TITLE_SPELLS, this.spellsTable.container);

        this.inventorySlotsTable = new InventorySlotsTable(dragAndDrop, this.inventorySlotTooltip);
        this.inventoryWindow = createWindow(TITLE_GLOBAL, this.inventorySlotsTable.container);

        this.equipSlotsTables = new HashMap<>(PartyContainer.MAXIMUM);
        for (HeroItem hero : Utils.getGameData().getParty().getAllHeroes()) {
            this.equipSlotsTables.put(hero.getId(), new EquipSlotsTable(hero, dragAndDrop, this.inventorySlotTooltip));
        }
        this.equipWindow = createWindow(TITLE_PERSONAL,
                                        this.equipSlotsTables.get(InventoryUtils.getSelectedHeroId()).equipSlots);

        this.skillsTable = new SkillsTable(this.personalityTooltip);
        this.skillsWindow = createWindow(TITLE_SKILLS, this.skillsTable.container);

        this.statsTable = new StatsTable(this.personalityTooltip);
        this.statsWindow = createWindow(TITLE_STATS, this.statsTable.table);

        this.calcsTable = new CalcsTable();
        this.calcsWindow = createWindow(TITLE_CALCS, this.calcsTable.table);

        this.heroesTable = new HeroesTable();
        this.heroesWindow = createWindow(TITLE_HEROES, this.heroesTable.heroes);
    }

    private static Window.WindowStyle createWindowStyle() {
        var texture = Utils.getResourceManager().getTextureAsset(SPRITE_BORDER);
        var ninepatch = new NinePatch(texture, 1, 1, 1, 1);
        var drawable = new NinePatchDrawable(ninepatch);
        BitmapFont font = Utils.getResourceManager().getTrueTypeAsset(TITLE_FONT, TITLE_SIZE);
        return new Window.WindowStyle(font, Color.BLACK, drawable);
    }

    void addToStage(Stage stage) {
        stage.addActor(spellsWindow);
        stage.addActor(inventoryWindow);
        stage.addActor(equipWindow);
        stage.addActor(skillsWindow);
        stage.addActor(statsWindow);
        stage.addActor(calcsWindow);
        stage.addActor(heroesWindow);
        inventorySlotTooltip.addToStage(stage);
        personalityTooltip.addToStage(stage);
    }

    void applyListeners(Stage stage) {
        spellsWindow.addListener(new ListenerMouseScrollPane(stage, spellsTable.scrollPane));
        inventoryWindow.addListener(new ListenerMouseScrollPane(stage, inventorySlotsTable.scrollPane));
        skillsWindow.addListener(new ListenerMouseScrollPane(stage, skillsTable.scrollPane));
    }

    void update() {
        spellsTable.update();
        skillsTable.update();
        statsTable.update();
        calcsTable.update();
        heroesTable.update();

        spellsWindow.pack();
        skillsWindow.pack();
        statsWindow.pack();
        calcsWindow.pack();
        heroesWindow.pack();
    }

    private Window createWindow(String title, Table table) {
        var window = new Window(title, createWindowStyle());
        window.add(table);
        window.padTop(TITLE_PADDING);
        window.getTitleLabel().setAlignment(Align.left);
        window.pack();
        return window;
    }

}
