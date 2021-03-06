package nl.t64.game.rpg.screens.inventory;

import com.badlogic.gdx.utils.GdxRuntimeException;
import lombok.Getter;
import nl.t64.game.rpg.Utils;
import nl.t64.game.rpg.components.party.HeroItem;
import nl.t64.game.rpg.components.party.PartyContainer;
import nl.t64.game.rpg.screens.shop.ShopScreen;


public final class InventoryUtils {

    @Getter
    private static HeroItem selectedHero = null;

    private InventoryUtils() {
        throw new IllegalCallerException("InventoryUtils class");
    }

    public static String getSelectedHeroId() {
        PartyContainer party = Utils.getGameData().getParty();
        if (!party.containsExactlyEqualTo(selectedHero)) {
            selectedHero = party.getHero(0);
        }
        return selectedHero.getId();
    }

    static void selectPreviousHero() {
        selectedHero = Utils.getGameData().getParty().getPreviousHero(selectedHero);
    }

    static void selectNextHero() {
        selectedHero = Utils.getGameData().getParty().getNextHero(selectedHero);
    }

    public static ScreenUI getScreenUI() {
        var currentScreen = Utils.getScreenManager().getCurrentScreen();
        if (currentScreen instanceof InventoryScreen inventoryScreen) {
            return inventoryScreen.inventoryUI;
        } else if (currentScreen instanceof ShopScreen shopScreen) {
            return shopScreen.getShopUI();
        }
        throw new GdxRuntimeException("currentScreen is instanceof: " + currentScreen);
    }

}
