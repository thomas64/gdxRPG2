package nl.t64.game.rpg.screens.inventory.itemslot;

import com.badlogic.gdx.scenes.scene2d.ui.Table;
import nl.t64.game.rpg.Utils;
import nl.t64.game.rpg.audio.AudioCommand;
import nl.t64.game.rpg.audio.AudioEvent;
import nl.t64.game.rpg.components.party.InventoryContainer;


public class ItemSlotSelector {

    private final InventoryContainer itemContainer;
    private final Table itemSlotsTable;

    private ItemSlot itemSlot;

    public ItemSlotSelector(InventoryContainer itemContainer, Table itemSlotsTable) {
        this.itemContainer = itemContainer;
        this.itemSlotsTable = itemSlotsTable;
    }

    public ItemSlot getCurrentSlot() {
        return itemSlot;
    }

    public void deselectCurrentSlot() {
        itemSlot.deselect();
    }

    public void selectCurrentSlot() {
        itemSlot.select();
    }

    public void findNextSlot() {
        int nextIndex = itemContainer.findNextFilledSlotFrom(itemSlot.index)
                                     .orElseGet(() -> itemContainer.findFirstFilledSlot()
                                                                   .orElse(itemSlot.index));
        selectNewSlot(nextIndex - itemSlot.index);
    }

    public void selectNewSlot(int newIndex) {
        int newSelected = itemSlot.index + newIndex;
        if (newSelected < 0 || newSelected >= itemContainer.getSize()) {
            return;
        }
        Utils.getAudioManager().handle(AudioCommand.SE_PLAY_ONCE, AudioEvent.SE_MENU_CURSOR);
        itemSlot.deselect();
        setNewSelectedByIndex(newSelected);
    }

    public void setNewSelectedByIndex(int index) {
        setNewCurrentByIndex(index);
        itemSlot.select();
    }

    public void setNewCurrentByIndex(int index) {
        itemSlot = (ItemSlot) itemSlotsTable.getChild(index);
    }

}
