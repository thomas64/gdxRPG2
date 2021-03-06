package nl.t64.game.rpg.screens.loot;

import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import nl.t64.game.rpg.Utils;
import nl.t64.game.rpg.audio.AudioCommand;
import nl.t64.game.rpg.audio.AudioEvent;
import nl.t64.game.rpg.components.loot.Loot;
import nl.t64.game.rpg.components.party.InventoryContainer;
import nl.t64.game.rpg.components.party.InventoryDatabase;
import nl.t64.game.rpg.components.party.InventoryGroup;
import nl.t64.game.rpg.components.party.InventoryItem;
import nl.t64.game.rpg.screens.inventory.inventoryslot.InventorySlot;
import nl.t64.game.rpg.screens.inventory.itemslot.InventoryImage;
import nl.t64.game.rpg.screens.inventory.itemslot.ItemSlotSelector;
import nl.t64.game.rpg.screens.inventory.tooltip.LootSlotTooltip;

import java.util.Map;
import java.util.stream.IntStream;


public class LootSlotsTable {

    private static final float SLOT_SIZE = 64f;
    private static final int NUMBER_OF_SLOTS = 8;
    private static final int SLOTS_IN_ROW = 4;
    private static final float INPUT_DELAY = 0.2f;

    final Table lootSlots;

    private final LootScreen lootScreen;
    private final Loot loot;
    private final LootSlotTooltip tooltip;
    private final InventoryContainer inventory;
    private final ItemSlotSelector selector;
    private final LootSlotTaker taker;

    LootSlotsTable(LootScreen lootScreen, Loot loot, LootSlotTooltip tooltip) {
        this.lootScreen = lootScreen;
        this.loot = loot;
        this.tooltip = tooltip;
        this.inventory = new InventoryContainer(NUMBER_OF_SLOTS);
        this.fillLootInventoryContainer();
        this.lootSlots = new Table();
        this.fillLootSlotsTable();

        this.selector = new ItemSlotSelector(this.inventory, this.lootSlots);
        this.taker = new LootSlotTaker(this.selector);

        this.lootSlots.addAction(Actions.sequence(Actions.delay(INPUT_DELAY),
                                                  Actions.run(this::selectFirstSlot),
                                                  Actions.addListener(this.createListener(), false)));
    }

    private void selectFirstSlot() {
        selector.setNewSelectedByIndex(0);
    }

    private LootSlotsTableListener createListener() {
        return new LootSlotsTableListener(this::closeScreen,
                                          this::toggleTooltip,
                                          this::takeItem,
                                          selector::selectNewSlot,
                                          SLOTS_IN_ROW);
    }

    private void takeItem() {
        if (inventory.isEmpty()) {
            closeScreen();
        } else {
            taker.take(selector.getCurrentSlot());
        }
    }

    private void closeScreen() {
        if (inventory.isEmpty()) {
            loot.clearContent();
        } else {
            loot.updateContent(inventory.getAllContent());
        }
        lootScreen.closeScreen(inventory.isEmpty());
    }

    private void toggleTooltip() {
        Utils.getAudioManager().handle(AudioCommand.SE_PLAY_ONCE, AudioEvent.SE_MENU_CONFIRM);
        tooltip.toggle(selector.getCurrentSlot());
    }

    private void fillLootInventoryContainer() {
        loot.getContent()
            .entrySet()
            .stream()
            .map(this::createInventoryItem)
            .forEach(inventory::autoSetItem);
    }

    private InventoryItem createInventoryItem(Map.Entry<String, Integer> loot) {
        return InventoryDatabase.getInstance().createInventoryItem(loot.getKey(), loot.getValue());
    }

    private void fillLootSlotsTable() {
        IntStream.range(0, inventory.getSize())
                 .forEach(this::createLootSlot);
    }

    private void createLootSlot(int index) {
        var lootSlot = new InventorySlot(index, InventoryGroup.LOOT_ITEM, tooltip, inventory);
        inventory.getItemAt(index)
                 .ifPresent(item -> lootSlot.addToStack(new InventoryImage(item)));
        lootSlots.add(lootSlot).size(SLOT_SIZE, SLOT_SIZE);
        if ((index + 1) % SLOTS_IN_ROW == 0) {
            lootSlots.row();
        }
    }

}
