package nl.t64.game.rpg.screens.shop;

import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import nl.t64.game.rpg.Utils;
import nl.t64.game.rpg.components.party.InventoryContainer;
import nl.t64.game.rpg.components.party.InventoryDatabase;
import nl.t64.game.rpg.components.party.InventoryItem;
import nl.t64.game.rpg.screens.inventory.InventoryUtils;
import nl.t64.game.rpg.screens.inventory.WindowSelector;
import nl.t64.game.rpg.screens.inventory.inventoryslot.InventorySlotsTableListener;
import nl.t64.game.rpg.screens.inventory.itemslot.InventoryImage;
import nl.t64.game.rpg.screens.inventory.itemslot.ItemSlot;
import nl.t64.game.rpg.screens.inventory.itemslot.ItemSlotSelector;
import nl.t64.game.rpg.screens.inventory.tooltip.ItemSlotTooltip;

import java.util.Optional;
import java.util.stream.IntStream;


public class ShopSlotsTable implements WindowSelector {

    private static final float SLOT_SIZE = 64f;
    private static final int NUMBER_OF_SLOTS = 99;
    private static final int SLOTS_IN_ROW = 9;
    private static final float CONTAINER_HEIGHT = 704f;

    final Table container;
    private final Table shopSlotTable;

    private final InventoryContainer inventory;
    private final ItemSlotTooltip tooltip;
    private final ItemSlotSelector selector;
    private final ShopSlotTaker taker;

    ShopSlotsTable(String shopId, ItemSlotTooltip tooltip) {
        this.inventory = new InventoryContainer(NUMBER_OF_SLOTS);
        this.fillShopContainer(shopId);
        this.tooltip = tooltip;
        this.shopSlotTable = new Table();
        this.fillShopSlots();
        this.refreshPurchaseColor();

        this.container = new Table();
        this.container.add(new ScrollPane(this.shopSlotTable)).height(CONTAINER_HEIGHT);
        this.container.setBackground(Utils.createTopBorder());

        this.selector = new ItemSlotSelector(this.inventory, this.shopSlotTable, SLOTS_IN_ROW);
        this.selector.setNewCurrentByIndex(0);
        this.taker = new ShopSlotTaker(this.selector);
        this.shopSlotTable.addListener(new InventorySlotsTableListener(selector::selectNewSlot, SLOTS_IN_ROW));
    }

    @Override
    public void setKeyboardFocus(Stage stage) {
        stage.setKeyboardFocus(shopSlotTable);
        InventoryUtils.setWindowSelected(container);
    }

    @Override
    public ItemSlot getCurrentSlot() {
        return selector.getCurrentSlot();
    }

    @Override
    public ItemSlotTooltip getCurrentTooltip() {
        return tooltip;
    }

    @Override
    public void deselectCurrentSlot() {
        selector.deselectCurrentSlot();
        InventoryUtils.setWindowDeselected(container);
    }

    @Override
    public void selectCurrentSlot() {
        selector.selectCurrentSlot();
    }

    @Override
    public void hideTooltip() {
        tooltip.hide();
    }

    @Override
    public void takeOne() {
        taker.buyOne(selector.getCurrentSlot());
    }

    @Override
    public void takeHalf() {
        taker.buyHalf(selector.getCurrentSlot());
    }

    @Override
    public void takeFull() {
        taker.buyFull(selector.getCurrentSlot());
    }

    public void refreshPurchaseColor() {
        shopSlotTable.getChildren()
                     .forEach(actor -> ((ShopSlot) actor).refreshPurchaseColor());
    }

    public Optional<ItemSlot> getPossibleSameStackableItemSlotWith(InventoryItem candidateItem) {
        if (candidateItem.isStackable()) {
            return inventory.findFirstSlotWithItem(candidateItem.getId())
                            .map(index -> (ItemSlot) shopSlotTable.getChild(index));
        } else {
            return Optional.empty();
        }
    }

    public Optional<ItemSlot> getPossibleEmptySlot() {
        return inventory.findFirstEmptySlot()
                        .map(index -> (ItemSlot) shopSlotTable.getChild(index));
    }

    private void fillShopContainer(String shopId) {
        Utils.getResourceManager().getShopInventory(shopId)
             .stream()
             .map(itemId -> InventoryDatabase.getInstance().createInventoryItemForShop(itemId))
             .forEach(inventory::autoSetItem);
    }

    private void fillShopSlots() {
        IntStream.range(0, inventory.getSize())
                 .forEach(this::createShopSlot);
    }

    private void createShopSlot(int index) {
        var shopSlot = new ShopSlot(index, tooltip, inventory);
        inventory.getItemAt(index)
                 .ifPresent(item -> shopSlot.addToStack(new InventoryImage(item)));
        shopSlotTable.add(shopSlot).size(SLOT_SIZE, SLOT_SIZE);
        if ((index + 1) % SLOTS_IN_ROW == 0) {
            shopSlotTable.row();
        }
    }

}
