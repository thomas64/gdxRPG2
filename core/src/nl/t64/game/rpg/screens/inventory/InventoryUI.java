package nl.t64.game.rpg.screens.inventory;

import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import nl.t64.game.rpg.Utils;
import nl.t64.game.rpg.screens.inventory.equipslot.EquipSlotsTables;
import nl.t64.game.rpg.screens.inventory.inventoryslot.InventorySlotsTable;
import nl.t64.game.rpg.screens.inventory.itemslot.ItemSlot;
import nl.t64.game.rpg.screens.inventory.tooltip.ItemSlotTooltip;
import nl.t64.game.rpg.screens.inventory.tooltip.PersonalityTooltip;

import java.util.List;


class InventoryUI implements ScreenUI {

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
    private final Window equipWindow;
    private final Window skillsWindow;
    private final Window statsWindow;
    private final Window calcsWindow;
    private final Window heroesWindow;

    private final SpellsTable spellsTable;
    private final InventorySlotsTable inventorySlotsTable;
    private final EquipSlotsTables equipSlotsTables;
    private final SkillsTable skillsTable;
    private final StatsTable statsTable;
    private final CalcsTable calcsTable;
    private final HeroesTable heroesTable;

    private final List<WindowSelector> tableList;

    private final ItemSlotTooltip itemSlotTooltip;
    private final PersonalityTooltip personalityTooltip;

    private int selectedTableIndex;


    InventoryUI(Stage stage) {
        this.itemSlotTooltip = new ItemSlotTooltip();
        this.personalityTooltip = new PersonalityTooltip();

        this.spellsTable = new SpellsTable(this.personalityTooltip);
        this.spellsWindow = Utils.createDefaultWindow(TITLE_SPELLS, this.spellsTable.container);

        this.inventorySlotsTable = new InventorySlotsTable(this.itemSlotTooltip);
        this.inventoryWindow = Utils.createDefaultWindow(TITLE_GLOBAL, this.inventorySlotsTable.container);

        this.equipSlotsTables = new EquipSlotsTables(this.itemSlotTooltip);
        this.equipWindow = Utils.createDefaultWindow(TITLE_PERSONAL, this.equipSlotsTables.getCurrentEquipTable());

        this.skillsTable = new SkillsTable(this.personalityTooltip);
        this.skillsWindow = Utils.createDefaultWindow(TITLE_SKILLS, this.skillsTable.container);

        this.statsTable = new StatsTable(this.personalityTooltip);
        this.statsWindow = Utils.createDefaultWindow(TITLE_STATS, this.statsTable.table);

        this.calcsTable = new CalcsTable();
        this.calcsWindow = Utils.createDefaultWindow(TITLE_CALCS, this.calcsTable.table);

        this.heroesTable = new HeroesTable();
        this.heroesWindow = Utils.createDefaultWindow(TITLE_HEROES, this.heroesTable.heroes);

        this.setWindowPositions();
        this.addToStage(stage);

        this.tableList = List.of(this.equipSlotsTables, this.inventorySlotsTable);
        this.selectedTableIndex = 1;
        this.setFocusOnSelectedTable();
    }

    @Override
    public EquipSlotsTables getEquipSlotsTables() {
        return equipSlotsTables;
    }

    @Override
    public InventorySlotsTable getInventorySlotsTable() {
        return inventorySlotsTable;
    }

    void updateSelectedHero(Runnable updateHero) {
        getSelectedTable().deselectCurrentSlot();

        int oldCurrentIndex = equipSlotsTables.getIndexOfCurrentSlot();
        equipWindow.getChild(1).remove();
        updateHero.run();
        equipWindow.add(equipSlotsTables.getCurrentEquipTable());
        equipSlotsTables.setCurrentByIndex(oldCurrentIndex);

        setFocusOnSelectedTable();
        getSelectedTable().selectCurrentSlot();
    }

    void selectPreviousTable() {
        getSelectedTable().deselectCurrentSlot();
        selectedTableIndex--;
        if (selectedTableIndex < 0) {
            selectedTableIndex = tableList.size() - 1;
        }
        setFocusOnSelectedTable();
        getSelectedTable().selectCurrentSlot();
    }

    void selectNextTable() {
        getSelectedTable().deselectCurrentSlot();
        selectedTableIndex++;
        if (selectedTableIndex >= tableList.size()) {
            selectedTableIndex = 0;
        }
        setFocusOnSelectedTable();
        getSelectedTable().selectCurrentSlot();
    }

    void reloadInventory() {
        inventorySlotsTable.clearAndFill();
    }

    void toggleTooltip() {
        ItemSlot currentSlot = getSelectedTable().getCurrentSlot();
        itemSlotTooltip.toggle(currentSlot);
    }

    void toggleCompare() {
        ItemSlot currentSlot = getSelectedTable().getCurrentSlot();
        itemSlotTooltip.toggleCompare(currentSlot);
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

    void unloadAssets() {
        heroesTable.disposePixmapTextures();
    }

    private void setFocusOnSelectedTable() {
        getSelectedTable().setKeyboardFocus(getStage());
        getStage().draw();
    }

    private WindowSelector getSelectedTable() {
        return tableList.get(selectedTableIndex);
    }

    private Stage getStage() {
        return equipWindow.getStage();
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
