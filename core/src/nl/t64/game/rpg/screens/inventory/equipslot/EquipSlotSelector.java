package nl.t64.game.rpg.screens.inventory.equipslot;

import nl.t64.game.rpg.Utils;
import nl.t64.game.rpg.audio.AudioCommand;
import nl.t64.game.rpg.audio.AudioEvent;
import nl.t64.game.rpg.screens.inventory.itemslot.ItemSlot;

import java.util.List;
import java.util.Optional;


class EquipSlotSelector {

    private final List<ItemSlot> equipSlotList;

    private ItemSlot itemSlot;

    EquipSlotSelector(List<ItemSlot> equipSlotList) {
        this.equipSlotList = equipSlotList;
    }

    ItemSlot getCurrentSlot() {
        return itemSlot;
    }

    void deselectCurrentSlot() {
        itemSlot.deselect();
    }

    void selectCurrentSlot() {
        itemSlot.select();
    }

    int getIndex() {
        return equipSlotList.indexOf(itemSlot);
    }

    void setNewCurrentByIndex(int index) {
        itemSlot = equipSlotList.get(index);
    }

    void trySelectNewSlot(int newIndex) {
        findItemSlotWithIndex(itemSlot.index + newIndex)
                .ifPresentOrElse(this::selectNewSlot,
                                 () -> trySelectNewSlot2(newIndex));

    }

    private void trySelectNewSlot2(int newIndex) {
        findItemSlotWithIndex(newIndex > 0 ? itemSlot.index + newIndex + EquipSlotsTableListener.SIDE_STEP
                                           : itemSlot.index + newIndex - EquipSlotsTableListener.SIDE_STEP)
                .ifPresent(this::selectNewSlot);
    }

    private void selectNewSlot(ItemSlot newSlot) {
        Utils.getAudioManager().handle(AudioCommand.SE_PLAY_ONCE, AudioEvent.SE_MENU_CURSOR);
        itemSlot.deselect();
        setNewSelected(newSlot);
    }

    private Optional<ItemSlot> findItemSlotWithIndex(int newSelectedIndex) {
        return equipSlotList.stream()
                            .filter(slot -> slot.index == newSelectedIndex)
                            .findFirst();
    }

    void setNewSelected(ItemSlot newSlot) {
        itemSlot = newSlot;
        itemSlot.select();
    }

}
