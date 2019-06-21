package nl.t64.game.rpg.screens.inventory;

import nl.t64.game.rpg.Utils;
import nl.t64.game.rpg.components.party.HeroItem;
import nl.t64.game.rpg.components.party.InventoryItem;
import nl.t64.game.rpg.components.party.PartyContainer;
import nl.t64.game.rpg.constants.ScreenType;


final class DynamicVars {

    static HeroItem selectedHero = null;
    static InventoryItem hoveredItem = null;

    private DynamicVars() {
        throw new IllegalStateException("DynamicVars class");
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

    static void updateHoveredItem(InventorySlot inventorySlot) {
        inventorySlot.getPossibleInventoryImage().ifPresentOrElse(
                DynamicVars::setHoveredItem,
                DynamicVars::clearHoveredItem);
    }

    static void clearHoveredItem() {
        hoveredItem = null;
    }

    private static void setHoveredItem(InventoryImage inventoryImage) {
        hoveredItem = inventoryImage.inventoryItem;
    }

}
