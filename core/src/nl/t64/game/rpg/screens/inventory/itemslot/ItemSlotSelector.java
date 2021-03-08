package nl.t64.game.rpg.screens.inventory.itemslot;

import com.badlogic.gdx.scenes.scene2d.ui.Table;
import nl.t64.game.rpg.Utils;
import nl.t64.game.rpg.audio.AudioCommand;
import nl.t64.game.rpg.audio.AudioEvent;
import nl.t64.game.rpg.components.party.InventoryContainer;


public class ItemSlotSelector {

    private final InventoryContainer itemContainer;
    private final Table itemSlotsTable;
    private final int slotsInRow;

    private ItemSlot itemSlot;

    public ItemSlotSelector(InventoryContainer itemContainer, Table itemSlotsTable, int slotsInRow) {
        this.itemContainer = itemContainer;
        this.itemSlotsTable = itemSlotsTable;
        this.slotsInRow = slotsInRow;
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

    public void selectNewSlot(int deltaIndex) {
        Utils.getAudioManager().handle(AudioCommand.SE_PLAY_ONCE, AudioEvent.SE_MENU_CURSOR);
        itemSlot.deselect();
        int newSelectedIndex = calculateNewSelectedIndex(deltaIndex);
        setNewSelectedByIndex(newSelectedIndex);
    }

    public void setNewSelectedByIndex(int index) {
        setNewCurrentByIndex(index);
        itemSlot.select();
    }

    public void setNewCurrentByIndex(int index) {
        itemSlot = (ItemSlot) itemSlotsTable.getChild(index);
    }

    private int calculateNewSelectedIndex(int deltaIndex) {
        int newIndex = itemSlot.index + deltaIndex;
        if (deltaIndex == 1 && newIndex % slotsInRow == 0) {
            newIndex -= slotsInRow;
        } else if (deltaIndex == -1 && (newIndex == -1 || newIndex % slotsInRow == slotsInRow - 1)) {
            newIndex += slotsInRow;
        }

        if (newIndex < 0) {
            newIndex = itemContainer.getSize() + newIndex;
        } else if (newIndex >= itemContainer.getSize()) {
            newIndex = newIndex - itemContainer.getSize();
        }
        return newIndex;
    }

}
