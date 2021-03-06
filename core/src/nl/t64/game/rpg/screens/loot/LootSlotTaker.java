package nl.t64.game.rpg.screens.loot;

import nl.t64.game.rpg.Utils;
import nl.t64.game.rpg.audio.AudioCommand;
import nl.t64.game.rpg.audio.AudioEvent;
import nl.t64.game.rpg.components.party.InventoryItem;
import nl.t64.game.rpg.screens.inventory.itemslot.ItemSlot;
import nl.t64.game.rpg.screens.inventory.itemslot.ItemSlotSelector;
import nl.t64.game.rpg.screens.inventory.messagedialog.MessageDialog;


class LootSlotTaker {

    private final ItemSlotSelector selector;

    private ItemSlot sourceSlot;

    LootSlotTaker(ItemSlotSelector selector) {
        this.selector = selector;
    }

    void take(ItemSlot itemSlot) {
        this.sourceSlot = itemSlot;
        tryPutLootSlotToInventorySlot();
    }

    private void tryPutLootSlotToInventorySlot() {
        sourceSlot.getPossibleInventoryImage()
                  .map(inventoryImage -> inventoryImage.inventoryItem)
                  .ifPresent(this::tryPutLootSlotToInventorySlot);
    }

    private void tryPutLootSlotToInventorySlot(InventoryItem candidateItem) {
        if (itemIsResource(candidateItem) || Utils.getGameData().getInventory().hasEmptySlot()) {
            putLootSlotToInventorySlot(candidateItem);
        } else {
            new MessageDialog("Inventory is full.").show(sourceSlot.getStage(), AudioEvent.SE_MENU_ERROR);
        }
    }

    private boolean itemIsResource(InventoryItem candidateItem) {
        return candidateItem.isStackable()
               && Utils.getGameData().getInventory().hasRoomForResource(candidateItem.getId());
    }

    private void putLootSlotToInventorySlot(InventoryItem candidateItem) {
        Utils.getAudioManager().handle(AudioCommand.SE_PLAY_ONCE, AudioEvent.SE_TAKE);
        Utils.getGameData().getInventory().autoSetItem(candidateItem);
        sourceSlot.clearStack();
        selector.findNextSlot();
    }

}
