package nl.t64.game.rpg.screens.world;

import nl.t64.game.rpg.Utils;
import nl.t64.game.rpg.components.loot.Loot;
import nl.t64.game.rpg.constants.ScreenType;
import nl.t64.game.rpg.screens.loot.RewardScreen;

import java.util.function.Consumer;


class RewardScreenLoader extends LootScreenLoader {

    RewardScreenLoader(Consumer<ScreenType> openLoadScreen, Loot reward) {
        super(openLoadScreen, reward);
    }

    @Override
    void openLootScreen() {
        var rewardScreen = (RewardScreen) Utils.getScreenManager().getScreen(ScreenType.REWARD);
        rewardScreen.setLoot(loot);
        rewardScreen.setLootTitle("   Reward");

        openLoadScreen.accept(ScreenType.REWARD_LOAD);
    }

}
