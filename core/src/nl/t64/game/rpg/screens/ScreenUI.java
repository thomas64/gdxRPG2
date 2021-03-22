package nl.t64.game.rpg.screens;

import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import lombok.AllArgsConstructor;
import nl.t64.game.rpg.screens.inventory.HeroesTable;
import nl.t64.game.rpg.screens.inventory.WindowSelector;
import nl.t64.game.rpg.screens.inventory.equipslot.EquipSlotsTables;
import nl.t64.game.rpg.screens.inventory.inventoryslot.InventorySlotsTable;
import nl.t64.game.rpg.screens.inventory.itemslot.ItemSlot;
import nl.t64.game.rpg.screens.inventory.tooltip.BaseTooltip;
import nl.t64.game.rpg.screens.shop.ShopSlotsTable;

import java.util.List;


@AllArgsConstructor
public abstract class ScreenUI {

    protected final Window equipWindow;

    protected final EquipSlotsTables equipSlotsTables;
    protected final InventorySlotsTable inventorySlotsTable;
    protected final HeroesTable heroesTable;

    protected final List<WindowSelector> tableList;
    protected int selectedTableIndex;

    public EquipSlotsTables getEquipSlotsTables() {
        return equipSlotsTables;
    }

    public InventorySlotsTable getInventorySlotsTable() {
        return inventorySlotsTable;
    }

    public ShopSlotsTable getShopSlotsTable() {
        throw new IllegalCallerException("ShopSlotsTable not implemented here.");
    }

    public void updateSelectedHero(Runnable updateHero) {
        getSelectedTable().deselectCurrentSlot();

        int oldCurrentIndex = equipSlotsTables.getIndexOfCurrentSlot();
        equipWindow.getChild(1).remove();
        updateHero.run();
        equipWindow.add(equipSlotsTables.getCurrentEquipTable());
        equipSlotsTables.setCurrentByIndex(oldCurrentIndex);

        setFocusOnSelectedTable();
        getSelectedTable().selectCurrentSlot();
    }

    public void selectPreviousTable() {
        getSelectedTable().hideTooltip();
        getSelectedTable().deselectCurrentSlot();
        selectedTableIndex--;
        if (selectedTableIndex < 0) {
            selectedTableIndex = tableList.size() - 1;
        }
        setFocusOnSelectedTable();
        getSelectedTable().selectCurrentSlot();
    }

    public void selectNextTable() {
        getSelectedTable().hideTooltip();
        getSelectedTable().deselectCurrentSlot();
        selectedTableIndex++;
        if (selectedTableIndex >= tableList.size()) {
            selectedTableIndex = 0;
        }
        setFocusOnSelectedTable();
        getSelectedTable().selectCurrentSlot();
    }

    public void toggleTooltip() {
        ItemSlot currentSlot = getSelectedTable().getCurrentSlot();
        BaseTooltip currentTooltip = getSelectedTable().getCurrentTooltip();
        currentTooltip.toggle(currentSlot);
    }

    public void toggleCompare() {
        ItemSlot currentSlot = getSelectedTable().getCurrentSlot();
        BaseTooltip currentTooltip = getSelectedTable().getCurrentTooltip();
        currentTooltip.toggleCompare(currentSlot);
    }

    public void unloadAssets() {
        heroesTable.disposePixmapTextures();
    }

    protected void setFocusOnSelectedTable() {
        getSelectedTable().setKeyboardFocus(getStage());
        getStage().draw();
    }

    protected WindowSelector getSelectedTable() {
        return tableList.get(selectedTableIndex);
    }

    private Stage getStage() {
        return equipWindow.getStage();
    }

}
