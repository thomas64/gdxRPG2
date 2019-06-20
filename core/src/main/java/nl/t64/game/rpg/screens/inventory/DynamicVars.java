package nl.t64.game.rpg.screens.inventory;

import nl.t64.game.rpg.Utils;
import nl.t64.game.rpg.components.party.InventoryItem;
import nl.t64.game.rpg.constants.ScreenType;


final class DynamicVars {

    static int heroIndex = 0;
    static InventoryItem hoveredItem = null;

    private DynamicVars() {
        throw new IllegalStateException("DynamicVars class");
    }

    static int getHeroIndex() {
        if (heroIndex > Utils.getGameData().getParty().getLastIndex()) {
            heroIndex = 0;
        }
        return heroIndex;
    }

    static void updateHeroIndex(int newHeroIndex) {
        var inventoryScreen = ((InventoryScreen) Utils.getScreenManager().getScreen(ScreenType.INVENTORY));
        var equipWindow = inventoryScreen.inventoryUI.equipWindow;
        var equipSlotsTables = inventoryScreen.inventoryUI.equipSlotsTables;

        equipWindow.removeActor(equipSlotsTables.get(heroIndex).equipSlots);
        heroIndex = newHeroIndex;
        equipWindow.add(equipSlotsTables.get(heroIndex).equipSlots);
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
