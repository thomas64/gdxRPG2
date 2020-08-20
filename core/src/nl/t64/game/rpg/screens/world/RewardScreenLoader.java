package nl.t64.game.rpg.screens.world;

import lombok.AllArgsConstructor;
import nl.t64.game.rpg.Utils;
import nl.t64.game.rpg.components.loot.Loot;
import nl.t64.game.rpg.constants.ScreenType;
import nl.t64.game.rpg.screens.loot.RewardScreen;

import java.util.function.Consumer;


@AllArgsConstructor
class RewardScreenLoader {

    final Consumer<ScreenType> openLoadScreen;
    final Loot loot;

    void openRewardScreen() {
        var rewardScreen = (RewardScreen) Utils.getScreenManager().getScreen(ScreenType.REWARD);
        rewardScreen.setLoot(loot);
        rewardScreen.setLootTitle("   Reward");

        openLoadScreen.accept(ScreenType.REWARD_LOAD);
    }

}
