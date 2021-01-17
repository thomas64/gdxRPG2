package nl.t64.game.rpg.screens.inventory;

import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.scenes.scene2d.utils.DragAndDrop;
import nl.t64.game.rpg.Utils;
import nl.t64.game.rpg.components.party.HeroItem;
import nl.t64.game.rpg.components.party.PartyContainer;
import nl.t64.game.rpg.components.tooltip.ItemSlotTooltip;
import nl.t64.game.rpg.components.tooltip.PersonalityTooltip;

import java.util.HashMap;
import java.util.Map;


class InventoryUI implements ScreenUI {

    private static final String TITLE_GLOBAL = "   Inventory";
    private static final String TITLE_PERSONAL = "   Equipment";
    private static final String TITLE_SPELLS = "   Spells";
    private static final String TITLE_SKILLS = "   Skills";
    private static final String TITLE_STATS = "   Stats";
    private static final String TITLE_CALCS = "   Calcs";
    private static final String TITLE_HEROES = "   Heroes";

    final Window spellsWindow;
    final Window inventoryWindow;
    final Window equipWindow;
    final Window skillsWindow;
    final Window statsWindow;
    final Window calcsWindow;
    final Window heroesWindow;

    private final SpellsTable spellsTable;
    private final InventorySlotsTable inventorySlotsTable;
    private final Map<String, EquipSlotsTable> equipSlotsTables;
    private final SkillsTable skillsTable;
    private final StatsTable statsTable;
    private final CalcsTable calcsTable;
    private final HeroesTable heroesTable;

    private final DragAndDrop dragAndDrop;

    private final ItemSlotTooltip itemSlotTooltip;
    private final PersonalityTooltip personalityTooltip;

    InventoryUI() {
        this.dragAndDrop = new DragAndDrop();
        this.itemSlotTooltip = new ItemSlotTooltip();
        this.personalityTooltip = new PersonalityTooltip();

        this.spellsTable = new SpellsTable(this.personalityTooltip);
        this.spellsWindow = Utils.createDefaultWindow(TITLE_SPELLS, this.spellsTable.container);

        this.inventorySlotsTable = new InventorySlotsTable(this.dragAndDrop, this.itemSlotTooltip);
        this.inventoryWindow = Utils.createDefaultWindow(TITLE_GLOBAL, this.inventorySlotsTable.container);

        this.equipSlotsTables = new HashMap<>(PartyContainer.MAXIMUM);
        this.fillEquipSlotsTables();
        final EquipSlotsTable equipTableOfSelectedHero = this.equipSlotsTables.get(InventoryUtils.getSelectedHeroId());
        this.equipWindow = Utils.createDefaultWindow(TITLE_PERSONAL, equipTableOfSelectedHero.container);

        this.skillsTable = new SkillsTable(this.personalityTooltip);
        this.skillsWindow = Utils.createDefaultWindow(TITLE_SKILLS, this.skillsTable.container);

        this.statsTable = new StatsTable(this.personalityTooltip);
        this.statsWindow = Utils.createDefaultWindow(TITLE_STATS, this.statsTable.table);

        this.calcsTable = new CalcsTable();
        this.calcsWindow = Utils.createDefaultWindow(TITLE_CALCS, this.calcsTable.table);

        this.heroesTable = new HeroesTable();
        this.heroesWindow = Utils.createDefaultWindow(TITLE_HEROES, this.heroesTable.heroes);
    }

    @Override
    public Window getEquipWindow() {
        return equipWindow;
    }

    @Override
    public Map<String, EquipSlotsTable> getEquipSlotsTables() {
        return equipSlotsTables;
    }

    @Override
    public InventorySlotsTable getInventorySlotsTable() {
        return inventorySlotsTable;
    }

    void reloadInventory() {
        dragAndDrop.clear();
        inventorySlotsTable.clearAndFill();
        equipSlotsTables.clear();
        fillEquipSlotsTables();
        equipWindow.getChildren().get(1).remove();
        equipWindow.add(equipSlotsTables.get(InventoryUtils.getSelectedHeroId()).container);
    }

    void addToStage(Stage stage) {
        stage.addActor(spellsWindow);
        stage.addActor(inventoryWindow);
        stage.addActor(equipWindow);
        stage.addActor(skillsWindow);
        stage.addActor(statsWindow);
        stage.addActor(calcsWindow);
        stage.addActor(heroesWindow);
        itemSlotTooltip.addToStage(stage);
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

    void unloadAssets() {
        heroesTable.disposePixmapTextures();
    }

    private void fillEquipSlotsTables() {
        for (HeroItem hero : Utils.getGameData().getParty().getAllHeroes()) {
            equipSlotsTables.put(hero.getId(), new EquipSlotsTable(hero, dragAndDrop, itemSlotTooltip));
        }
    }

}
