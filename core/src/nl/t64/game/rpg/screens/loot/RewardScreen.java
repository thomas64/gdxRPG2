package nl.t64.game.rpg.screens.loot;


public class RewardScreen extends LootScreen {

    @Override
    void resolveAfterClearingContent() {
        notifyRewardTaken();
    }

}
