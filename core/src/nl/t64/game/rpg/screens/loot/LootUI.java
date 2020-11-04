package nl.t64.game.rpg.screens.loot;

import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.scenes.scene2d.utils.DragAndDrop;
import nl.t64.game.rpg.Utils;
import nl.t64.game.rpg.components.loot.Loot;
import nl.t64.game.rpg.screens.inventory.EquipSlotsTable;
import nl.t64.game.rpg.screens.inventory.InventorySlotsTable;
import nl.t64.game.rpg.screens.inventory.ListenerMouseScrollPane;
import nl.t64.game.rpg.screens.inventory.ScreenUI;
import nl.t64.game.rpg.screens.inventory.tooltip.ItemSlotTooltip;
import nl.t64.game.rpg.screens.inventory.tooltip.LootSlotTooltip;

import java.util.Map;


class LootUI implements ScreenUI {

    private static final String TITLE_GLOBAL = "   Inventory";
    private static final float INVENTORY_HEIGHT = 224f;

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
        this.inventoryWindow = Utils.createDefaultWindow(TITLE_GLOBAL, this.inventorySlotsTable.container);
        this.inventoryWindow.setMovable(false);

        this.lootSlotsTable = new LootSlotsTable(loot, this.dragAndDrop, this.lootSlotTooltip);
        this.lootWindow = Utils.createDefaultWindow(title, this.lootSlotsTable.container);
        this.lootWindow.setMovable(false);
    }

    @Override
    public Window getEquipWindow() {
        throw new IllegalCallerException("Double click loot cannot reach this.");
    }

    @Override
    public Map<String, EquipSlotsTable> getEquipSlotsTables() {
        return Map.of();
    }

    @Override
    public InventorySlotsTable getInventorySlotsTable() {
        return inventorySlotsTable;
    }

    boolean takeItem() {
        return lootSlotsTable.hasTakenItem();
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

}
