package nl.t64.game.rpg.screens.loot;

import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.DragAndDrop;
import com.badlogic.gdx.scenes.scene2d.utils.NinePatchDrawable;
import nl.t64.game.rpg.Utils;
import nl.t64.game.rpg.components.loot.Content;
import nl.t64.game.rpg.components.loot.Loot;
import nl.t64.game.rpg.components.party.InventoryContainer;
import nl.t64.game.rpg.components.party.InventoryDatabase;
import nl.t64.game.rpg.components.party.InventoryGroup;
import nl.t64.game.rpg.components.party.InventoryItem;
import nl.t64.game.rpg.screens.inventory.*;
import nl.t64.game.rpg.screens.inventory.tooltip.ItemSlotTooltip;
import nl.t64.game.rpg.screens.inventory.tooltip.ItemSlotTooltipListener;

import java.util.List;
import java.util.OptionalInt;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import java.util.stream.IntStream;


public class LootSlotsTable {

    private static final String SPRITE_TOP_BORDER = "sprites/top_border.png";
    private static final float SLOT_SIZE = 64f;
    private static final int NUMBER_OF_SLOTS = 8;
    private static final int SLOTS_IN_ROW = 4;
    private static final float CONTAINER_HEIGHT = 128f;

    final Table container;
    final ScrollPane scrollPane;
    private final Table lootSlotTable;
    private final DragAndDrop dragAndDrop;
    private final ItemSlotTooltip tooltip;
    private final InventoryContainer inventory;

    LootSlotsTable(Loot loot, DragAndDrop dragAndDrop, ItemSlotTooltip tooltip) {
        this.inventory = new InventoryContainer(NUMBER_OF_SLOTS);
        this.fillLootContainer(loot);
        this.dragAndDrop = dragAndDrop;
        this.tooltip = tooltip;
        this.lootSlotTable = new Table();
        fillLootSlots();

        this.scrollPane = new ScrollPane(this.lootSlotTable);
        this.container = new Table();
        this.container.add(this.scrollPane).height(CONTAINER_HEIGHT);
        setTopBorder();
    }

    boolean isEmpty() {
        return inventory.isEmpty();
    }

    List<Content> getContent() {
        return inventory.getAllFilledSlots()
                        .stream()
                        .map(this::createContent)
                        .collect(Collectors.toList());
    }

    private Content createContent(InventoryItem inventoryItem) {
        return new Content(inventoryItem.getId(), inventoryItem.getAmount());
    }

    boolean takeItem() {
        OptionalInt firstFilledSlot = inventory.findFirstFilledSlot();
        boolean wasPresent = firstFilledSlot.isPresent();
        firstFilledSlot.ifPresent(index -> {
            var lootSlot = (InventorySlot) lootSlotTable.getChildren().get(index);
            DoubleClickHandler.handleShop(lootSlot, dragAndDrop);
        });
        return wasPresent;
    }

    private void fillLootContainer(Loot loot) {
        loot.getContent()
            .stream()
            .map(this::createInventoryItem)
            .forEach(inventory::autoSetItem);
    }

    private InventoryItem createInventoryItem(Content content) {
        return InventoryDatabase.getInstance().createInventoryItem(content.getItemId(), content.getAmount());
    }

    private void fillLootSlots() {
        IntStream.range(0, inventory.getSize())
                 .forEach(this::createLootSlot);
    }

    private void setTopBorder() {
        var texture = Utils.getResourceManager().getTextureAsset(SPRITE_TOP_BORDER);
        var ninepatch = new NinePatch(texture, 0, 0, 1, 0);
        var drawable = new NinePatchDrawable(ninepatch);
        container.setBackground(drawable);
    }

    private void createLootSlot(int index) {
        InventorySlot lootSlot = new InventorySlot(index, InventoryGroup.LOOT_ITEM, inventory);
        lootSlot.addListener(new ItemSlotTooltipListener(tooltip));
        lootSlot.addListener(new ItemSlotClickListener(DoubleClickHandler::handleShop, dragAndDrop));
        dragAndDrop.addTarget(new ItemSlotTarget(lootSlot));
        dragAndDrop.addSource(new ItemSlotSource(lootSlot.amountLabel, dragAndDrop));
        inventory.getItemAt(index).ifPresent(addToSlot(lootSlot));
        lootSlotTable.add(lootSlot).size(SLOT_SIZE, SLOT_SIZE);
        if ((index + 1) % SLOTS_IN_ROW == 0) {
            lootSlotTable.row();
        }
    }

    private Consumer<InventoryItem> addToSlot(InventorySlot lootSlot) {
        return inventoryItem -> {
            var inventoryImage = new InventoryImage(inventoryItem);
            lootSlot.addToStack(inventoryImage);
            var itemSlotSource = new ItemSlotSource(inventoryImage, dragAndDrop);
            dragAndDrop.addSource(itemSlotSource);
        };
    }
}
