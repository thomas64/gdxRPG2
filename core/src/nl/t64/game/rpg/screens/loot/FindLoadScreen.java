package nl.t64.game.rpg.screens.loot;

import com.badlogic.gdx.scenes.scene2d.ui.Image;
import nl.t64.game.rpg.Utils;
import nl.t64.game.rpg.constants.ScreenType;


public class FindLoadScreen extends LootLoadScreen {

    @Override
    protected void setScreen() {
        var findScreen = (FindScreen) Utils.getScreenManager().getScreen(ScreenType.FIND);
        findScreen.setBackground((Image) stage.getActors().get(0),
                                 (Image) stage.getActors().get(1));
        Utils.getScreenManager().setScreen(ScreenType.FIND);
    }

}
