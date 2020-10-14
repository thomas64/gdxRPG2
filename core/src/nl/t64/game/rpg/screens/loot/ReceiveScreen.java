package nl.t64.game.rpg.screens.loot;

import nl.t64.game.rpg.Utils;
import nl.t64.game.rpg.components.loot.Loot;
import nl.t64.game.rpg.constants.ScreenType;


public class ReceiveScreen extends LootScreen {

    public static void load(Loot receive) {
        var receiveScreen = (ReceiveScreen) Utils.getScreenManager().getScreen(ScreenType.RECEIVE);
        receiveScreen.loot = receive;
        receiveScreen.lootTitle = "   Receive";
        Utils.getScreenManager().openParchmentLoadScreen(ScreenType.RECEIVE);
    }

    @Override
    void resolveAfterClearingContent() {
        notifyReceiveTaken();
    }

}
