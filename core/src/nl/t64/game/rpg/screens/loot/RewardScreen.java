package nl.t64.game.rpg.screens.loot;

import nl.t64.game.rpg.Utils;
import nl.t64.game.rpg.components.loot.Loot;
import nl.t64.game.rpg.constants.ScreenType;


public class RewardScreen extends LootScreen {

    public static void load(Loot reward) {
        var rewardScreen = (RewardScreen) Utils.getScreenManager().getScreen(ScreenType.REWARD);
        rewardScreen.loot = reward;
        rewardScreen.lootTitle = "   Reward";
        Utils.getScreenManager().openParchmentLoadScreen(ScreenType.REWARD);
    }

    @Override
    void resolveAfterClearingContent() {
        notifyRewardTaken();
    }

}
