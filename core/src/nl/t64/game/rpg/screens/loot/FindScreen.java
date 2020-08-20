package nl.t64.game.rpg.screens.loot;


public class FindScreen extends LootScreen {

    @Override
    void resolveAfterClearingContent() {
        notifyLootTaken();
    }

}
