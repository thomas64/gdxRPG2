package nl.t64.game.rpg.screens.loot;

import com.badlogic.gdx.scenes.scene2d.ui.Image;
import nl.t64.game.rpg.Utils;
import nl.t64.game.rpg.constants.ScreenType;


public class RewardLoadScreen extends LootLoadScreen {

    @Override
    protected void setScreen() {
        var rewardScreen = (RewardScreen) Utils.getScreenManager().getScreen(ScreenType.REWARD);
        rewardScreen.setBackground((Image) stage.getActors().get(0),
                                   (Image) stage.getActors().get(1));
        Utils.getScreenManager().setScreen(ScreenType.REWARD);
    }

}
