package nl.t64.game.rpg.screens.shop;

import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.DragAndDrop;
import nl.t64.game.rpg.Utils;
import nl.t64.game.rpg.components.party.InventoryContainer;
import nl.t64.game.rpg.components.party.InventoryDatabase;
import nl.t64.game.rpg.components.party.InventoryItem;
import nl.t64.game.rpg.constants.InventoryGroup;
import nl.t64.game.rpg.screens.inventory.*;
import nl.t64.game.rpg.screens.inventory.tooltip.ItemSlotTooltip;
import nl.t64.game.rpg.screens.inventory.tooltip.ItemSlotTooltipListener;

import java.util.function.Consumer;
import java.util.stream.IntStream;


class ShopSlotsTable {

    private static final float SLOT_SIZE = 64f;
    private static final int SLOTS_IN_ROW = 6;
    private static final float CONTAINER_HEIGHT = 704f;

    final Table container;
    final ScrollPane scrollPane;
    private final Table shopSlotTable;
    private final DragAndDrop dragAndDrop;
    private final ItemSlotTooltip tooltip;
    private final InventoryContainer inventory;

    ShopSlotsTable(String shopId, DragAndDrop dragAndDrop, ItemSlotTooltip tooltip) {
        this.inventory = new InventoryContainer();
        this.fillShopContainer(shopId);
        this.dragAndDrop = dragAndDrop;
        this.tooltip = tooltip;
        this.shopSlotTable = new Table();
        fillShopSlots();

        this.scrollPane = new ScrollPane(this.shopSlotTable);
        this.container = new Table();
        this.container.add(this.scrollPane).height(CONTAINER_HEIGHT);
        container.setBackground(Utils.createTopBorder());
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
        InventorySlot shopSlot = new InventorySlot(index, InventoryGroup.SHOP_ITEM, inventory);
        shopSlot.addListener(new ItemSlotTooltipListener(tooltip));
        shopSlot.addListener(new ItemSlotClickListener(DoubleClickHandler::handleShop, dragAndDrop));
        dragAndDrop.addTarget(new ItemSlotTarget(shopSlot));
        dragAndDrop.addSource(new ItemSlotSource(shopSlot.amountLabel, dragAndDrop));
        inventory.getItemAt(index).ifPresent(addToSlot(shopSlot));
        shopSlotTable.add(shopSlot).size(SLOT_SIZE, SLOT_SIZE);
        if ((index + 1) % SLOTS_IN_ROW == 0) {
            shopSlotTable.row();
        }
    }

    private Consumer<InventoryItem> addToSlot(InventorySlot shopslot) {
        return inventoryItem -> {
            var inventoryImage = new InventoryImage(inventoryItem);
            makeDraggable(inventoryImage);
            shopslot.addToStack(inventoryImage);
        };
    }

    private void makeDraggable(InventoryImage inventoryImage) {
        var itemSlotSource = new ItemSlotSource(inventoryImage, dragAndDrop);
        dragAndDrop.addSource(itemSlotSource);
    }

}
