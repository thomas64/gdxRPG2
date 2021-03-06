package nl.t64.game.rpg.screens.inventory.inventoryslot;

import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import nl.t64.game.rpg.Utils;
import nl.t64.game.rpg.components.party.InventoryContainer;
import nl.t64.game.rpg.components.party.InventoryGroup;
import nl.t64.game.rpg.components.party.InventoryItem;
import nl.t64.game.rpg.screens.inventory.WindowSelector;
import nl.t64.game.rpg.screens.inventory.itemslot.InventoryImage;
import nl.t64.game.rpg.screens.inventory.itemslot.ItemSlot;
import nl.t64.game.rpg.screens.inventory.itemslot.ItemSlotSelector;
import nl.t64.game.rpg.screens.inventory.tooltip.ItemSlotTooltip;

import java.util.Optional;
import java.util.stream.IntStream;


public class InventorySlotsTable implements WindowSelector {

    private static final float SLOT_SIZE = 64f;
    private static final int SLOTS_IN_ROW = 6;
    private static final float CONTAINER_HEIGHT = 704f;
    private static final float INPUT_DELAY = 0.2f;

    public final Table container;
    private final Table inventorySlotTable;

    private final InventoryContainer inventory;
    private final ItemSlotTooltip tooltip;
    private final ItemSlotSelector selector;
    private final InventorySlotTaker taker;

    public InventorySlotsTable(ItemSlotTooltip tooltip) {
        this.inventory = Utils.getGameData().getInventory();
        this.tooltip = tooltip;
        this.inventorySlotTable = new Table();
        this.fillInventorySlots();

        this.container = new Table();
        this.container.add(new ScrollPane(this.inventorySlotTable)).height(CONTAINER_HEIGHT);
        this.container.setBackground(Utils.createTopBorder());

        this.selector = new ItemSlotSelector(this.inventory, this.inventorySlotTable);
        this.taker = new InventorySlotTaker(this.selector);

        this.inventorySlotTable.addAction(Actions.sequence(Actions.delay(INPUT_DELAY),
                                                           Actions.run(this::selectFirstSlot),
                                                           Actions.addListener(this.createListener(), false)));
    }

    private void selectFirstSlot() {
        selector.setNewSelectedByIndex(0);
    }

    private InventorySlotsTableListener createListener() {
        return new InventorySlotsTableListener(this::equipItem, selector::selectNewSlot, SLOTS_IN_ROW);
    }

    private void equipItem() {
        taker.equip(selector.getCurrentSlot());
    }

    @Override
    public void setKeyboardFocus(Stage stage) {
        stage.setKeyboardFocus(inventorySlotTable);
    }

    @Override
    public ItemSlot getCurrentSlot() {
        return selector.getCurrentSlot();
    }

    @Override
    public void deselectCurrentSlot() {
        selector.deselectCurrentSlot();
    }

    @Override
    public void selectCurrentSlot() {
        selector.selectCurrentSlot();
    }

    public void addResource(InventoryItem inventoryItem) {
        inventory.autoSetItem(inventoryItem);
        clearAndFill();
    }

    public void removeResource(String itemId, int amount) {
        inventory.autoRemoveItem(itemId, amount);
        clearAndFill();
    }

    public void clearAndFill() {
        int index = selector.getCurrentSlot().index;
        boolean isSelected = selector.getCurrentSlot().isSelected();
        inventorySlotTable.clear();
        fillInventorySlots();
        inventorySlotTable.addListener(createListener());
        inventorySlotTable.getStage().draw();
        if (isSelected) {
            selector.setNewSelectedByIndex(index);
        } else {
            selector.setNewCurrentByIndex(index);
        }
    }

    public Optional<ItemSlot> getPossibleSameStackableItemSlotWith(InventoryItem candidateItem) {
        if (candidateItem.isStackable()) {
            return inventory.findFirstSlotWithItem(candidateItem.getId())
                            .map(index -> (ItemSlot) inventorySlotTable.getChild(index));
        } else {
            return Optional.empty();
        }
    }

    public Optional<ItemSlot> getPossibleEmptySlot() {
        return inventory.findFirstEmptySlot()
                        .map(index -> (ItemSlot) inventorySlotTable.getChild(index));
    }

    private void fillInventorySlots() {
        IntStream.range(0, inventory.getSize())
                 .forEach(this::createInventorySlot);
    }

    private void createInventorySlot(int index) {
        var inventorySlot = new InventorySlot(index, InventoryGroup.EVERYTHING, tooltip, inventory);
        inventory.getItemAt(index)
                 .ifPresent(item -> inventorySlot.addToStack(new InventoryImage(item)));
        inventorySlotTable.add(inventorySlot).size(SLOT_SIZE, SLOT_SIZE);
        if ((index + 1) % SLOTS_IN_ROW == 0) {
            inventorySlotTable.row();
        }
    }

}