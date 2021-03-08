package nl.t64.game.rpg.screens.inventory;

import lombok.Getter;
import nl.t64.game.rpg.Utils;
import nl.t64.game.rpg.components.party.HeroItem;
import nl.t64.game.rpg.components.party.PartyContainer;
import nl.t64.game.rpg.screens.ScreenUI;


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

    public static void selectPreviousHero() {
        selectedHero = Utils.getGameData().getParty().getPreviousHero(selectedHero);
    }

    public static void selectNextHero() {
        selectedHero = Utils.getGameData().getParty().getNextHero(selectedHero);
    }

    public static ScreenUI getScreenUI() {
        return Utils.getScreenManager().getCurrentScreenToLoad().getScreenUI();
    }

}
