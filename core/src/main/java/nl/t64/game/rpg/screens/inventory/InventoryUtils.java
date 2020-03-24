package nl.t64.game.rpg.screens.inventory;

import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.GdxRuntimeException;
import lombok.Getter;
import nl.t64.game.rpg.Utils;
import nl.t64.game.rpg.components.party.HeroItem;
import nl.t64.game.rpg.components.party.PartyContainer;
import nl.t64.game.rpg.screens.shop.ShopScreen;


public final class InventoryUtils {

    @Getter
    private static HeroItem selectedHero = null;
    @Getter
    private static boolean shiftPressed = false;

    private InventoryUtils() {
        throw new IllegalStateException("InventoryUtils class");
    }

    public static String getSelectedHeroId() {
        PartyContainer party = Utils.getGameData().getParty();
        if (!party.contains(selectedHero)) {
            selectedHero = party.getHero(0);
        }
        return selectedHero.getId();
    }

    static void updateSelectedHero(HeroItem newSelectedHero) {
        final var screenUI = getScreenUI();
        final Table oldEquipSlots = screenUI.getEquipSlotsTables().get(selectedHero.getId()).equipSlots;
        screenUI.getEquipWindow().removeActor(oldEquipSlots);
        selectedHero = newSelectedHero;
        final Table newEquipSlots = screenUI.getEquipSlotsTables().get(selectedHero.getId()).equipSlots;
        screenUI.getEquipWindow().add(newEquipSlots);
    }

    public static void handleDoubleClickInventory(InventorySlot clickedSlot) {
        clickedSlot.getPossibleInventoryImage()
                   .ifPresent(inventoryImage -> {
                       getScreenUI().getEquipSlotsTables().get(selectedHero.getId())
                                    .getPossibleSlotOfGroup(inventoryImage.inventoryGroup)
                                    .ifPresent(targetSlot -> {
                                        clickedSlot.decrementAmount();
                                        new InventorySlotsExchanger(inventoryImage, clickedSlot, targetSlot).exchange();
                                    });
                   });
    }

    public static void handleDoubleClickEquipOrShop(InventorySlot clickedSlot) {
        clickedSlot.getPossibleInventoryImage()
                   .ifPresent(inventoryImage -> {
                       getScreenUI().getInventorySlotsTable()
                                    .getPossibleEmptySlot()
                                    .ifPresent(targetSlot -> {
                                        clickedSlot.decrementAmount();
                                        new InventorySlotsExchanger(inventoryImage, clickedSlot, targetSlot).exchange();
                                    });
                   });
    }

    static void setShiftPressed(boolean isPressed) {
        shiftPressed = isPressed;
    }

    static ScreenUI getScreenUI() {
        var currentScreen = Utils.getScreenManager().getCurrentScreen();
        if (currentScreen instanceof InventoryScreen) {
            return ((InventoryScreen) currentScreen).inventoryUI;
        } else if (currentScreen instanceof ShopScreen) {
            return ((ShopScreen) currentScreen).getShopUI();
        }
        throw new GdxRuntimeException("currentScreen is instanceof: " + currentScreen);
    }

}
