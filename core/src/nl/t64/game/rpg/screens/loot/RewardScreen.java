package nl.t64.game.rpg.screens.loot;

import com.badlogic.gdx.utils.Null;
import nl.t64.game.rpg.Utils;
import nl.t64.game.rpg.audio.AudioEvent;
import nl.t64.game.rpg.components.loot.Loot;
import nl.t64.game.rpg.constants.ScreenType;
import nl.t64.game.rpg.screens.inventory.MessageDialog;


public class RewardScreen extends LootScreen {

    @Null
    private String levelUpMessage;

    public static void load(Loot reward, @Null String levelUpMessage) {
        var rewardScreen = (RewardScreen) Utils.getScreenManager().getScreen(ScreenType.REWARD);
        rewardScreen.loot = reward;
        rewardScreen.lootTitle = "   Reward";
        rewardScreen.levelUpMessage = levelUpMessage;
        Utils.getScreenManager().openParchmentLoadScreen(ScreenType.REWARD);
    }

    @Override
    public void show() {
        super.show();
        if (levelUpMessage != null) {
            showLevelUpMessage();
        }
    }

    @Override
    void resolveAfterClearingContent() {
        notifyRewardTaken();
    }

    private void showLevelUpMessage() {
        final var messageDialog = new MessageDialog(levelUpMessage);
        messageDialog.setLeftAlignment();
        messageDialog.show(stage, AudioEvent.SE_LEVELUP);
    }

}
