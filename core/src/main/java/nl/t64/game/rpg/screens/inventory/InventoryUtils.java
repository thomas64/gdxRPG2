package nl.t64.game.rpg.screens.inventory;

import nl.t64.game.rpg.Utils;
import nl.t64.game.rpg.components.party.HeroItem;
import nl.t64.game.rpg.components.party.PartyContainer;
import nl.t64.game.rpg.constants.ScreenType;


final class InventoryUtils {

    static HeroItem selectedHero = null;
    static boolean shiftPressed = false;

    private InventoryUtils() {
        throw new IllegalStateException("InventoryUtils class");
    }

    static String getSelectedHeroId() {
        PartyContainer party = Utils.getGameData().getParty();
        if (!party.contains(selectedHero)) {
            selectedHero = party.getHero(0);
        }
        return selectedHero.getId();
    }

    static void updateSelectedHero(HeroItem newSelectedHero) {
        var inventoryScreen = ((InventoryScreen) Utils.getScreenManager().getScreen(ScreenType.INVENTORY));
        var equipWindow = inventoryScreen.inventoryUI.equipWindow;
        var equipSlotsTables = inventoryScreen.inventoryUI.equipSlotsTables;

        equipWindow.removeActor(equipSlotsTables.get(selectedHero.getId()).equipSlots);
        selectedHero = newSelectedHero;
        equipWindow.add(equipSlotsTables.get(selectedHero.getId()).equipSlots);
    }

    static void handleDoubleClickInventory(InventorySlot clickedSlot) {
        clickedSlot.getPossibleInventoryImage().ifPresent(inventoryImage -> {
            var inventoryScreen = ((InventoryScreen) Utils.getScreenManager().getScreen(ScreenType.INVENTORY));
            var equipSlotsTable = inventoryScreen.inventoryUI.equipSlotsTables.get(selectedHero.getId());
            equipSlotsTable.getPossibleSlotOfGroup(inventoryImage.inventoryGroup).ifPresent(targetSlot -> {
                clickedSlot.decrementAmount();
                new InventorySlotsExchanger(inventoryImage, clickedSlot, targetSlot).exchange();
            });
        });
    }

    static void handleDoubleClickEquip(InventorySlot clickedSlot) {
        clickedSlot.getPossibleInventoryImage().ifPresent(inventoryImage -> {
            var inventoryScreen = ((InventoryScreen) Utils.getScreenManager().getScreen(ScreenType.INVENTORY));
            var inventorySlotsTable = inventoryScreen.inventoryUI.inventorySlotsTable;
            inventorySlotsTable.getPossibleEmptySlot().ifPresent(targetSlot -> {
                clickedSlot.decrementAmount();
                new InventorySlotsExchanger(inventoryImage, clickedSlot, targetSlot).exchange();
            });
        });
    }

    static void setShiftPressed(boolean isPressed) {
        shiftPressed = isPressed;
    }

}
