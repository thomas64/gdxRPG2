package nl.t64.game.rpg.screens.loot;

import nl.t64.game.rpg.Utils;
import nl.t64.game.rpg.components.loot.Loot;
import nl.t64.game.rpg.constants.ScreenType;


public class FindScreen extends LootScreen {

    public static void load(Loot loot) {
        var findScreen = (FindScreen) Utils.getScreenManager().getScreen(ScreenType.FIND);
        findScreen.loot = loot;
        findScreen.lootTitle = "   Found";
        Utils.getScreenManager().openParchmentLoadScreen(ScreenType.FIND);
    }

    @Override
    void resolveAfterClearingContent() {
        notifyLootTaken();
    }

}
