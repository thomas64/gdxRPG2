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
    @Getter
    private static boolean ctrlPressed = false;

    private InventoryUtils() {
        throw new IllegalStateException("InventoryUtils class");
    }

    public static String getSelectedHeroId() {
        PartyContainer party = Utils.getGameData().getParty();
        if (!party.containsExactlyEqualTo(selectedHero)) {
            selectedHero = party.getHero(0);
        }
        return selectedHero.getId();
    }

    static void selectPreviousHero() {
        final HeroItem newSelectedHero = Utils.getGameData().getParty().getPreviousHero(selectedHero);
        updateSelectedHero(newSelectedHero);
    }

    static void selectNextHero() {
        final HeroItem newSelectedHero = Utils.getGameData().getParty().getNextHero(selectedHero);
        updateSelectedHero(newSelectedHero);
    }

    static void updateSelectedHero(HeroItem newSelectedHero) {
        final var screenUI = getScreenUI();
        final Table oldEquipSlots = screenUI.getEquipSlotsTables().get(selectedHero.getId()).equipSlotTable;
        screenUI.getEquipWindow().removeActor(oldEquipSlots);
        selectedHero = newSelectedHero;
        final Table newEquipSlots = screenUI.getEquipSlotsTables().get(selectedHero.getId()).equipSlotTable;
        screenUI.getEquipWindow().add(newEquipSlots);
    }

    public static void setShiftPressed(boolean isPressed) {
        shiftPressed = isPressed;
    }

    public static void setCtrlPressed(boolean isPressed) {
        ctrlPressed = isPressed;
    }

    static ScreenUI getScreenUI() {
        var currentScreen = Utils.getScreenManager().getCurrentScreen();
        if (currentScreen instanceof InventoryScreen inventoryScreen) {
            return inventoryScreen.inventoryUI;
        } else if (currentScreen instanceof ShopScreen shopScreen) {
            return shopScreen.getShopUI();
        }
        throw new GdxRuntimeException("currentScreen is instanceof: " + currentScreen);
    }

}
