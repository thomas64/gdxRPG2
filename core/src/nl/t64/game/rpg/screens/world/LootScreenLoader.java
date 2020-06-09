package nl.t64.game.rpg.screens.world;

import lombok.AllArgsConstructor;
import nl.t64.game.rpg.Utils;
import nl.t64.game.rpg.components.loot.Loot;
import nl.t64.game.rpg.constants.ScreenType;
import nl.t64.game.rpg.screens.loot.LootScreen;

import java.util.function.Consumer;


@AllArgsConstructor
class LootScreenLoader {

    private final Consumer<ScreenType> openLoadScreen;
    private final Loot loot;

    void openLootScreen() {
        var lootScreen = (LootScreen) Utils.getScreenManager().getScreen(ScreenType.LOOT);
        lootScreen.setLoot(loot);
        lootScreen.setLootTitle("   Found");

        openLoadScreen.accept(ScreenType.LOOT_LOAD);
    }

}
