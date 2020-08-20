package nl.t64.game.rpg.screens.world;

import lombok.AllArgsConstructor;
import nl.t64.game.rpg.Utils;
import nl.t64.game.rpg.components.loot.Loot;
import nl.t64.game.rpg.constants.ScreenType;
import nl.t64.game.rpg.screens.loot.FindScreen;

import java.util.function.Consumer;


@AllArgsConstructor
class FindScreenLoader {

    final Consumer<ScreenType> openLoadScreen;
    final Loot loot;

    void openFindScreen() {
        var findScreen = (FindScreen) Utils.getScreenManager().getScreen(ScreenType.FIND);
        findScreen.setLoot(loot);
        findScreen.setLootTitle("   Found");

        openLoadScreen.accept(ScreenType.FIND_LOAD);
    }

}
