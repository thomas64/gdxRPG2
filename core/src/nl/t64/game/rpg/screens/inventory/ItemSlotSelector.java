package nl.t64.game.rpg.screens.inventory;

import com.badlogic.gdx.scenes.scene2d.ui.Table;
import nl.t64.game.rpg.Utils;
import nl.t64.game.rpg.audio.AudioCommand;
import nl.t64.game.rpg.audio.AudioEvent;
import nl.t64.game.rpg.components.party.InventoryContainer;
import nl.t64.game.rpg.components.tooltip.ItemSlotTooltip;


public class ItemSlotSelector {

    private final InventoryContainer itemContainer;
    private final ItemSlotTooltip tooltip;
    private final Table itemSlotsTable;

    private ItemSlot itemSlot;

    public ItemSlotSelector(InventoryContainer itemContainer, ItemSlotTooltip tooltip, Table itemSlotsTable) {
        this.itemContainer = itemContainer;
        this.tooltip = tooltip;
        this.itemSlotsTable = itemSlotsTable;
    }

    public ItemSlot getSelectedSlot() {
        return itemSlot;
    }

    public void findNextSlot() {
        int nextIndex = itemContainer.findNextFilledSlotFrom(itemSlot.getIndex())
                                     .orElseGet(() -> itemContainer.findFirstFilledSlot()
                                                                   .orElse(itemSlot.getIndex()));
        selectNewSlot(nextIndex - itemSlot.getIndex());
    }

    public void selectNewSlot(int newIndex) {
        int newSelected = itemSlot.getIndex() + newIndex;
        if (newSelected < 0 || newSelected >= itemContainer.getSize()) {
            return;
        }
        Utils.getAudioManager().handle(AudioCommand.SE_PLAY_ONCE, AudioEvent.SE_MENU_CURSOR);
        itemSlot.setDeselected();
        setSelected(newSelected);
    }

    public void setSelected(int index) {
        itemSlot = (ItemSlot) itemSlotsTable.getChild(index);
        itemSlot.setSelected();
        tooltip.refresh(itemSlot);
    }

    public void toggleTooltip() {
        tooltip.toggle(itemSlot);
    }

}
