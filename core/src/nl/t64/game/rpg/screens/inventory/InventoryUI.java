package nl.t64.game.rpg.screens.inventory;

import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import nl.t64.game.rpg.Utils;
import nl.t64.game.rpg.screens.ScreenUI;
import nl.t64.game.rpg.screens.inventory.equipslot.EquipSlotsTables;
import nl.t64.game.rpg.screens.inventory.inventoryslot.InventorySlotsTable;
import nl.t64.game.rpg.screens.inventory.tooltip.ItemSlotTooltip;
import nl.t64.game.rpg.screens.inventory.tooltip.PersonalityTooltip;

import java.util.List;


class InventoryUI extends ScreenUI {

    private static final float SPELLS_WINDOW_POSITION_X = 1483f;
    private static final float SPELLS_WINDOW_POSITION_Y = 50f;
    private static final float INVENTORY_WINDOW_POSITION_X = 1062f;
    private static final float INVENTORY_WINDOW_POSITION_Y = 50f;
    private static final float EQUIP_WINDOW_POSITION_X = 736f;
    private static final float EQUIP_WINDOW_POSITION_Y = 50f;
    private static final float SKILLS_WINDOW_POSITION_X = 395f;
    private static final float SKILLS_WINDOW_POSITION_Y = 50f;
    private static final float STATS_WINDOW_POSITION_X = 63f;
    private static final float STATS_WINDOW_POSITION_Y = 429f;
    private static final float CALCS_WINDOW_POSITION_X = 63f;
    private static final float CALCS_WINDOW_POSITION_Y = 50f;
    private static final float HEROES_WINDOW_POSITION_X = 63f;
    private static final float HEROES_WINDOW_POSITION_Y = 834f;

    private static final String TITLE_GLOBAL = "   Inventory";
    private static final String TITLE_PERSONAL = "   Equipment";
    private static final String TITLE_SPELLS = "   Spells";
    private static final String TITLE_SKILLS = "   Skills";
    private static final String TITLE_STATS = "   Stats";
    private static final String TITLE_CALCS = "   Calcs";
    private static final String TITLE_HEROES = "   Heroes";

    private final Window spellsWindow;
    private final Window inventoryWindow;
    private final Window skillsWindow;
    private final Window statsWindow;
    private final Window calcsWindow;
    private final Window heroesWindow;

    private final SpellsTable spellsTable;
    private final SkillsTable skillsTable;
    private final StatsTable statsTable;
    private final CalcsTable calcsTable;

    private final ItemSlotTooltip itemSlotTooltip;
    private final PersonalityTooltip personalityTooltip;

    private InventoryUI(Stage stage, ItemSlotTooltip itemSlotTooltip, PersonalityTooltip personalityTooltip,
                        Window spellsWindow, Window inventoryWindow, Window equipWindow, Window skillsWindow,
                        Window statsWindow, Window calcsWindow, Window heroesWindow,
                        SpellsTable spellsTable, InventorySlotsTable inventorySlotsTable, EquipSlotsTables equipSlotsTables,
                        SkillsTable skillsTable, StatsTable statsTable, CalcsTable calcsTable, HeroesTable heroesTable,
                        List<WindowSelector> tableList, int selectedTableIndex) {
        super(equipWindow, equipSlotsTables, inventorySlotsTable, heroesTable, tableList, selectedTableIndex);
        this.itemSlotTooltip = itemSlotTooltip;
        this.personalityTooltip = personalityTooltip;
        this.spellsWindow = spellsWindow;
        this.inventoryWindow = inventoryWindow;
        this.skillsWindow = skillsWindow;
        this.statsWindow = statsWindow;
        this.calcsWindow = calcsWindow;
        this.heroesWindow = heroesWindow;
        this.spellsTable = spellsTable;
        this.skillsTable = skillsTable;
        this.statsTable = statsTable;
        this.calcsTable = calcsTable;

        this.setWindowPositions();
        this.addToStage(stage);
        this.setFocusOnSelectedTable();
        this.getSelectedTable().selectCurrentSlot();
    }

    static InventoryUI create(Stage stage) {

        var itemSlotTooltip = new ItemSlotTooltip();
        var personalityTooltip = new PersonalityTooltip();

        var spellsTable = new SpellsTable(personalityTooltip);
        var spellsWindow = Utils.createDefaultWindow(TITLE_SPELLS, spellsTable.container);

        var inventorySlotsTable = new InventorySlotsTable(itemSlotTooltip);
        var inventoryWindow = Utils.createDefaultWindow(TITLE_GLOBAL, inventorySlotsTable.container);

        var equipSlotsTables = new EquipSlotsTables(itemSlotTooltip);
        var equipWindow = Utils.createDefaultWindow(TITLE_PERSONAL, equipSlotsTables.getCurrentEquipTable());

        var skillsTable = new SkillsTable(personalityTooltip);
        var skillsWindow = Utils.createDefaultWindow(TITLE_SKILLS, skillsTable.container);

        var statsTable = new StatsTable(personalityTooltip);
        var statsWindow = Utils.createDefaultWindow(TITLE_STATS, statsTable.container);

        var calcsTable = new CalcsTable(personalityTooltip);
        var calcsWindow = Utils.createDefaultWindow(TITLE_CALCS, calcsTable.container);

        var heroesTable = new HeroesTable();
        var heroesWindow = Utils.createDefaultWindow(TITLE_HEROES, heroesTable.heroes);

        List<WindowSelector> tableList = List.of(statsTable, calcsTable, skillsTable,
                                                 equipSlotsTables, inventorySlotsTable, spellsTable);
        int selectedTableIndex = 4;

        return new InventoryUI(stage, itemSlotTooltip, personalityTooltip,
                               spellsWindow, inventoryWindow, equipWindow, skillsWindow,
                               statsWindow, calcsWindow, heroesWindow,
                               spellsTable, inventorySlotsTable, equipSlotsTables, skillsTable,
                               statsTable, calcsTable, heroesTable,
                               tableList, selectedTableIndex);
    }

    void doAction() {
        getSelectedTable().doAction();
    }

    void reloadInventory() {
        inventorySlotsTable.clearAndFill();
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

    private void setWindowPositions() {
        spellsWindow.setPosition(SPELLS_WINDOW_POSITION_X, SPELLS_WINDOW_POSITION_Y);
        inventoryWindow.setPosition(INVENTORY_WINDOW_POSITION_X, INVENTORY_WINDOW_POSITION_Y);
        equipWindow.setPosition(EQUIP_WINDOW_POSITION_X, EQUIP_WINDOW_POSITION_Y);
        skillsWindow.setPosition(SKILLS_WINDOW_POSITION_X, SKILLS_WINDOW_POSITION_Y);
        statsWindow.setPosition(STATS_WINDOW_POSITION_X, STATS_WINDOW_POSITION_Y);
        calcsWindow.setPosition(CALCS_WINDOW_POSITION_X, CALCS_WINDOW_POSITION_Y);
        heroesWindow.setPosition(HEROES_WINDOW_POSITION_X, HEROES_WINDOW_POSITION_Y);
    }

    private void addToStage(Stage stage) {
        itemSlotTooltip.addToStage(stage);
        personalityTooltip.addToStage(stage);
        stage.addActor(spellsWindow);
        stage.addActor(inventoryWindow);
        stage.addActor(equipWindow);
        stage.addActor(skillsWindow);
        stage.addActor(statsWindow);
        stage.addActor(calcsWindow);
        stage.addActor(heroesWindow);
    }

}
