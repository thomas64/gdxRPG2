package nl.t64.game.rpg.screens.inventory.equipslot;

import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import nl.t64.game.rpg.Utils;
import nl.t64.game.rpg.components.party.PartyContainer;
import nl.t64.game.rpg.screens.inventory.InventoryUtils;
import nl.t64.game.rpg.screens.inventory.WindowSelector;
import nl.t64.game.rpg.screens.inventory.itemslot.ItemSlot;
import nl.t64.game.rpg.screens.inventory.tooltip.ItemSlotTooltip;

import java.util.HashMap;
import java.util.Map;


public class EquipSlotsTables implements WindowSelector {

    private final ItemSlotTooltip tooltip;
    private final Map<String, EquipSlotsTable> equipSlots;

    public EquipSlotsTables(ItemSlotTooltip tooltip) {
        this.tooltip = tooltip;
        this.equipSlots = new HashMap<>(PartyContainer.MAXIMUM);
        this.fillEquipSlotsTables();
        this.setCurrentByIndex(7);
    }

    @Override
    public void setKeyboardFocus(Stage stage) {
        stage.setKeyboardFocus(getCurrentEquipTable());
    }

    @Override
    public ItemSlot getCurrentSlot() {
        return getCurrentEquipSlots().getCurrentSlot();
    }

    @Override
    public ItemSlotTooltip getCurrentTooltip() {
        return tooltip;
    }

    @Override
    public void deselectCurrentSlot() {
        getCurrentEquipSlots().deselectCurrentSlot();
    }

    @Override
    public void selectCurrentSlot() {
        getCurrentEquipSlots().selectCurrentSlot();
    }

    @Override
    public void doAction() {
        getCurrentEquipSlots().dequipItem();
    }

    @Override
    public void hideTooltip() {
        tooltip.hide();
    }

    public int getIndexOfCurrentSlot() {
        return getCurrentEquipSlots().getIndexOfCurrentSlot();
    }

    public void setCurrentByIndex(int index) {
        getCurrentEquipSlots().setCurrentByIndex(index);
    }

    public Table getCurrentEquipTable() {
        return getCurrentEquipSlots().container;
    }

    public EquipSlotsTable getCurrentEquipSlots() {
        return equipSlots.get(InventoryUtils.getSelectedHeroId());
    }

    private void fillEquipSlotsTables() {
        Utils.getGameData().getParty().getAllHeroes()
             .forEach(hero -> equipSlots.put(hero.getId(), new EquipSlotsTable(hero, tooltip)));
    }

}
