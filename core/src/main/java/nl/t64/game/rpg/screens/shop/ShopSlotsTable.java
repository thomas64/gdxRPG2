package nl.t64.game.rpg.screens.shop;

import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.DragAndDrop;
import com.badlogic.gdx.scenes.scene2d.utils.NinePatchDrawable;
import nl.t64.game.rpg.Utils;
import nl.t64.game.rpg.components.party.InventoryContainer;
import nl.t64.game.rpg.components.party.InventoryDatabase;
import nl.t64.game.rpg.components.party.InventoryGroup;
import nl.t64.game.rpg.components.tooltip.InventorySlotTooltip;
import nl.t64.game.rpg.components.tooltip.InventorySlotTooltipListener;
import nl.t64.game.rpg.screens.inventory.*;

import java.util.stream.IntStream;


class ShopSlotsTable {

    private static final String SPRITE_TOP_BORDER = "sprites/top_border.png";
    private static final float SLOT_SIZE = 64f;
    private static final int SLOTS_IN_ROW = 6;
    private static final float CONTAINER_HEIGHT = 704f;

    final Table container;
    final ScrollPane scrollPane;
    private final Table inventorySlots;
    private final DragAndDrop dragAndDrop;
    private final InventorySlotTooltip tooltip;
    private final InventoryContainer inventory;

    ShopSlotsTable(String shopId, DragAndDrop dragAndDrop, InventorySlotTooltip tooltip) {
        this.inventory = new InventoryContainer();
        this.fillShopContainer(shopId);
        this.dragAndDrop = dragAndDrop;
        this.tooltip = tooltip;
        this.inventorySlots = new Table();
        fillInventorySlots();

        this.scrollPane = new ScrollPane(this.inventorySlots);
        this.container = new Table();
        this.container.add(this.scrollPane).height(CONTAINER_HEIGHT);
        setTopBorder();
    }

    private void fillShopContainer(String shopId) {
        Utils.getResourceManager().getShopInventory(shopId)
             .stream()
             .map(itemId -> InventoryDatabase.getInstance().getInventoryItem(itemId))
             .forEach(inventory::autoSetItem);
    }

    private void fillInventorySlots() {
        IntStream.range(0, inventory.getSize())
                 .forEach(this::createInventorySlot);
    }

    private void setTopBorder() {
        var texture = Utils.getResourceManager().getTextureAsset(SPRITE_TOP_BORDER);
        var ninepatch = new NinePatch(texture, 0, 0, 1, 0);
        var drawable = new NinePatchDrawable(ninepatch);
        container.setBackground(drawable);
    }

    private void createInventorySlot(int index) {
        var inventorySlot = new InventorySlot(InventoryGroup.SHOP_EQUIP_ITEM);
        inventorySlot.addListener(new InventorySlotTooltipListener(tooltip));
        inventorySlot.addListener(new InventorySlotClickListener(InventoryUtils::handleDoubleClickEquipOrShop));
        dragAndDrop.addTarget(new InventorySlotTarget(inventorySlot));
        dragAndDrop.addSource(new InventorySlotSource(inventorySlot.amountLabel, dragAndDrop));
        inventory.getItemAt(index).ifPresent(inventoryItem -> {
            int newAmount = inventory.getAmountOfItemAt(index);
            inventorySlot.setAmount(newAmount);
            inventorySlot.addToStack(new InventoryImage(inventoryItem));
            dragAndDrop.addSource(new InventorySlotSource(inventorySlot.getCertainInventoryImage(), dragAndDrop));
        });
        inventorySlots.add(inventorySlot).size(SLOT_SIZE, SLOT_SIZE);
        if ((index + 1) % SLOTS_IN_ROW == 0) {
            inventorySlots.row();
        }
    }

}
