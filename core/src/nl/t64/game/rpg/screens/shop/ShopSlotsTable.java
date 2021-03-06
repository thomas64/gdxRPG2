package nl.t64.game.rpg.screens.shop;

import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import nl.t64.game.rpg.Utils;
import nl.t64.game.rpg.components.party.InventoryContainer;
import nl.t64.game.rpg.components.party.InventoryDatabase;
import nl.t64.game.rpg.components.party.InventoryGroup;
import nl.t64.game.rpg.screens.inventory.inventoryslot.InventorySlot;
import nl.t64.game.rpg.screens.inventory.itemslot.InventoryImage;
import nl.t64.game.rpg.screens.inventory.tooltip.ItemSlotTooltip;

import java.util.stream.IntStream;


class ShopSlotsTable {

    private static final float SLOT_SIZE = 64f;
    private static final int SLOTS_IN_ROW = 6;
    private static final float CONTAINER_HEIGHT = 704f;

    final Table container;
    final ScrollPane scrollPane;
    private final Table shopSlotTable;
    private final ItemSlotTooltip tooltip;
    private final InventoryContainer inventory;

    ShopSlotsTable(String shopId, ItemSlotTooltip tooltip) {
        this.inventory = new InventoryContainer();
        this.fillShopContainer(shopId);
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
        InventorySlot shopSlot = new InventorySlot(index, InventoryGroup.SHOP_ITEM, tooltip, inventory);
        inventory.getItemAt(index)
                 .ifPresent(inventoryItem -> shopSlot.addToStack(new InventoryImage(inventoryItem)));
        shopSlotTable.add(shopSlot).size(SLOT_SIZE, SLOT_SIZE);
        if ((index + 1) % SLOTS_IN_ROW == 0) {
            shopSlotTable.row();
        }
    }

}
