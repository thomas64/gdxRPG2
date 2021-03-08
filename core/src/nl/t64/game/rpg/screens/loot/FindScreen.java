package nl.t64.game.rpg.screens.loot;

import nl.t64.game.rpg.Utils;
import nl.t64.game.rpg.audio.AudioCommand;
import nl.t64.game.rpg.audio.AudioEvent;
import nl.t64.game.rpg.components.loot.Loot;
import nl.t64.game.rpg.constants.ScreenType;


public class FindScreen extends LootScreen {

    public static void load(Loot loot, AudioEvent event) {
        Utils.getAudioManager().handle(AudioCommand.SE_PLAY_ONCE, event);
        var findScreen = (FindScreen) Utils.getScreenManager().getScreen(ScreenType.FIND);
        findScreen.loot = loot;
        findScreen.lootTitle = "   Found";
        Utils.getScreenManager().openParchmentLoadScreen(ScreenType.FIND);
    }

    @Override
    void resolveAfterClearingContent() {
        lootSubject.notifyLootTaken();
    }

}
