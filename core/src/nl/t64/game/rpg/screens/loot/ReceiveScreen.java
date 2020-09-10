package nl.t64.game.rpg.screens.loot;


public class ReceiveScreen extends LootScreen {

    @Override
    void resolveAfterClearingContent() {
        notifyReceiveTaken();
    }

}
